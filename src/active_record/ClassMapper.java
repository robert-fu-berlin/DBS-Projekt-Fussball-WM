package active_record;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableBiMap.Builder;

class ClassMapper<A extends ActiveRecord> {

	private final String LEFT_COLUMN = "cheesecake", RIGHT_COLUMN = "cherrybomb";

	// TODO: add bimap mapping column names to member names and back

	private final String tablename;

	private final ImmutableBiMap<String, Field> columnMap;

	private final ImmutableBiMap<String, Field> oneToMany;

	private final ActiveRecordMapper mapper;

	private Field idField;

	private final Class<A> mappedClass;

	private boolean built = false;

	public ClassMapper(Class<A> activeRecord, ActiveRecordMapper mapper, String prefix) {
		this.tablename = (prefix != null ? prefix + "_" : "") + javaToUnderscore(activeRecord.getSimpleName());
		this.mappedClass = activeRecord;
		this.mapper = mapper;

		// Obtain a list of all fields the class has access to.
		List<Field> fields = new ArrayList<Field>();
		List<Field> sets = new ArrayList<Field>();

		Class<?> c = activeRecord;
		while (c != null) {
			Collections.addAll(fields, c.getDeclaredFields());
			c = c.getSuperclass();
		}

		// Filter static and synthetic fields, ensure valid types, add sets as
		// one to many relationships
		java.util.Iterator<Field> iterator = fields.iterator();
		while (iterator.hasNext()) {
			Field f = iterator.next();

			Class<?> type = f.getType();

			if (f.isSynthetic() || (f.getModifiers() & (Modifier.TRANSIENT | Modifier.STATIC)) != 0) {
				iterator.remove();
				continue;
			}

			if (type.isEnum())
				continue;

			if (TypeMapper.hasMapping(type))
				continue;

			if (type.isAssignableFrom(Set.class)) {
				ParameterizedType set = (ParameterizedType) f.getGenericType();
				Class<?> setType = (Class<?>) set.getActualTypeArguments()[0];

				if (f.isAnnotationPresent(Inverse.class)) {
					if (ActiveRecord.class.isAssignableFrom(setType)) {
						iterator.remove();
						continue;
					} else
						throw new IllegalArgumentException("Type of inverse must be an ActiveRecord");
				}
				if (TypeMapper.hasMapping(setType)) {
					sets.add(f);
					iterator.remove();
					continue;
				}
			}
			throw new IllegalArgumentException("Given type " + type + " " + "of field " + f + " is not mappable");
		}

		ImmutableBiMap.Builder<String, Field> columnsBuilder = new Builder<String, Field>();
		for (Field field : fields) {
			if (sets.contains(field))
				assert false;

			if (field.getName().equals("id")) {
				assert idField == null;
				idField = field;
			}

			columnsBuilder.put(javaToUnderscore(field.getName()), field);
		}
		columnMap = columnsBuilder.build();

		ImmutableBiMap.Builder<String, Field> oneToManyBuilder = new Builder<String, Field>();
		for (Field field : sets) {
			if (field.isAnnotationPresent(Inverse.class))
				oneToManyBuilder.put(tableNameForInverse(field), field);
			else
				oneToManyBuilder.put(tablename + "_" + javaToUnderscore(field.getName()), field);
		}

		this.oneToMany = oneToManyBuilder.build();
	}

	public void createTable(Connection connection) throws SQLException {
		if (connection.isClosed())
			throw new IllegalArgumentException("Connection " + connection + "is closed.");

		if (built || exists(connection, tablename)) {
			return;
		}

		Map<String, String> columnsAndSqlTypes = new HashMap<String, String>(columnMap.size());

		Set<Field> primitive = new HashSet<Field>();
		Set<Field> foreignKeys = new HashSet<Field>();

		for (Field f : columnMap.values()) {
			if (!ActiveRecord.class.isAssignableFrom(f.getType()))
				primitive.add(f);
			else
				foreignKeys.add(f);
		}

		for (Field f : primitive) {
			if (f == idField) {
				columnsAndSqlTypes.put(columnMap.inverse().get(f), "serial8 primary key");
				continue;
			}

			columnsAndSqlTypes.put(columnMap.inverse().get(f), TypeMapper.postgresForJava(f.getType()));
		}

		for (Field f : foreignKeys)
			columnsAndSqlTypes.put(columnMap.inverse().get(f), TypeMapper.postgresForJava(f.getType()));

		String sql = "create table " + tablename + " ("
		+ Joiner.on(", ").withKeyValueSeparator(" ").join(columnsAndSqlTypes) + ");";

		Statement statement = connection.createStatement();
		statement.execute(sql);

		statement.close();

		built = true;

		// Create directly referenced tables
		for (Field f : foreignKeys) {
			ClassMapper<?> foreignClassMapper = mapper.getClassMapperForClass((Class<? extends ActiveRecord>)f.getType());
			foreignClassMapper.createTable(connection);
		}

		// Create tables referenced over one-to-many associations
		for (Field f : oneToMany.values()) {
			ParameterizedType set = (ParameterizedType) f.getGenericType();
			Class<?> setType = (Class<?>) set.getActualTypeArguments()[0];

			if (!ActiveRecord.class.isAssignableFrom(setType))
				continue;

			ClassMapper<? extends ActiveRecord> classMapper = mapper.getClassMapperForClass((Class<? extends ActiveRecord>) setType);
			classMapper.createTable(connection);
		}

		for (Field f : foreignKeys) {
			String columnName = columnMap.inverse().get(f);
			String constraintName = tablename + "_" + columnName + "_fk";
			String referencedTable = mapper.getClassMapperForClass((Class<? extends ActiveRecord>)f.getType()).tablename;

			Statement addForeign = connection.createStatement();
			String sqlForeign = "alter table " + tablename + " add constraint " + constraintName + " foreign key (" + columnName + ") references " + referencedTable + " (id);";

			addForeign.execute(sqlForeign);
			addForeign.close();
		}

		createAssociated(connection);
	}

	/**
	 * Helper method of createTable().
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private void createAssociated(Connection connection) throws SQLException {
		// XXX Find better name for columns
		for (Entry<String, Field> entry : oneToMany.entrySet()) {

			if (exists(connection, entry.getKey()))
				continue;

			Field f = entry.getValue();

			boolean inverse = f.isAnnotationPresent(Inverse.class);

			Class<?> type = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];

			String referencedTableName = null;

			if (ActiveRecord.class.isAssignableFrom(type)) {
				ClassMapper<? extends ActiveRecord> inverseMapper = mapper
				.getClassMapperForClass((Class<? extends ActiveRecord>) type);
				referencedTableName = mapper.getClassMapperForClass((Class<? extends ActiveRecord>) type).tablename;
			}

			StringBuffer sqlStatement = new StringBuffer();
			sqlStatement.append("create table ");
			sqlStatement.append(entry.getKey());
			sqlStatement.append(" (");

			if (inverse) {
				sqlStatement.append(LEFT_COLUMN);
				sqlStatement.append(" bigint references ");
				sqlStatement.append(referencedTableName);
				sqlStatement.append(" on delete cascade, ");
				sqlStatement.append(RIGHT_COLUMN);
				sqlStatement.append(" bigint references ");
				sqlStatement.append(tablename);
				sqlStatement.append(" on delete cascade,");
			} else {
				sqlStatement.append(LEFT_COLUMN);
				sqlStatement.append(" bigint references ");
				sqlStatement.append(tablename);
				sqlStatement.append(" on delete cascade, ");
				if (referencedTableName != null) {
					sqlStatement.append(RIGHT_COLUMN);
					sqlStatement.append(" bigint references ");
					sqlStatement.append(referencedTableName);
					sqlStatement.append(" on delete cascade,");
				} else {
					sqlStatement.append(RIGHT_COLUMN);
					sqlStatement.append(",");
				}
			}
			sqlStatement.append(" primary key (");
			sqlStatement.append(LEFT_COLUMN);
			sqlStatement.append(",");
			sqlStatement.append(RIGHT_COLUMN);
			sqlStatement.append("));");
			Statement statement = connection.createStatement();
			statement.execute(sqlStatement.toString());
			statement.close();
		}
	}

	public void dropTable(Connection connection) throws SQLException {

		if (!built)
			return;

		String sql = null;
		Statement statement = null;
		Set<Field> foreignKeys = new HashSet<Field>();

		for (Entry<String, Field> entry : oneToMany.entrySet()) {
			if (!exists(connection, entry.getKey()))
				continue;
			sql = "drop table " + entry.getKey() + ";";
			statement = connection.createStatement();
			statement.execute(sql);
			statement.close();
		}

		sql = "drop table " + tablename + " cascade;";

		statement = connection.createStatement();
		statement.execute(sql);
		statement.close();

		built = false;

		for (Field f : columnMap.values()) {
			if (ActiveRecord.class.isAssignableFrom(f.getType()))
				foreignKeys.add(f);
		}

		for (Field f : foreignKeys) {
			ClassMapper<?> foreignClassMapper = mapper.getClassMapperForClass((Class<? extends ActiveRecord>)f.getType());
			foreignClassMapper.dropTable(connection);
		}

		for (Field f : oneToMany.values()) {
			ParameterizedType set = (ParameterizedType) f.getGenericType();
			Class<?> setType = (Class<?>) set.getActualTypeArguments()[0];

			if (!ActiveRecord.class.isAssignableFrom(setType))
				continue;

			ClassMapper<? extends ActiveRecord> classMapper = mapper.getClassMapperForClass((Class<? extends ActiveRecord>) setType);
			classMapper.dropTable(connection);
		}

	}

	public void save(Connection connection, A record) throws SQLException {
		if (!record.validateAssociated().isEmpty())
			throw new IllegalStateException(record.validate().get(0).toString()); // XXX

		if (record.getId() == null)
			insert(connection, record);
		else
			update(connection, record);

		// handle one-to-many relations

		for (Entry<String, Field> entry : oneToMany.entrySet()) {
			Field f = entry.getValue();
			String relation = entry.getKey();
			Set<? extends ActiveRecord> set;
			boolean inverse = f.isAnnotationPresent(Inverse.class);
			try {
				f.setAccessible(true);
				set = (Set<? extends ActiveRecord>) f.get(record);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException();
			} catch (IllegalAccessException e) {
				throw new IllegalStateException();
			}

			String deleteSql = "delete from " + relation + " where " + (inverse ? RIGHT_COLUMN : LEFT_COLUMN) + " = "
			+ record.getId() + ";";
			Statement deleteStatement = connection.createStatement();
			deleteStatement.execute(deleteSql);

			if (set == null || set.isEmpty())
				continue;

			StringBuffer sqlStatement = new StringBuffer("Insert into ");
			sqlStatement.append(relation);
			sqlStatement.append(" (");
			if (inverse) {
				sqlStatement.append(RIGHT_COLUMN);
				sqlStatement.append(",");
				sqlStatement.append(LEFT_COLUMN);
			} else {
				sqlStatement.append(LEFT_COLUMN);
				sqlStatement.append(",");
				sqlStatement.append(RIGHT_COLUMN);
			}
			sqlStatement.append(") values ");

			boolean first = true;
			for (ActiveRecord a : set) {
				/*
				 * Save <code>a</code> if it hasn't been saved before.
				 */
				if (a.getId() == null) {
					ClassMapper classMapper = mapper.getClassMapperForClass(a.getClass());
					classMapper.save(connection, a);
				}

				if (!first)
					sqlStatement.append(", ");
				sqlStatement.append("(");
				sqlStatement.append(record.getId());
				sqlStatement.append(",");
				sqlStatement.append(a.getId());
				sqlStatement.append(")");
				first = false;
			}
			sqlStatement.append(";");
			Statement insertStatement = connection.createStatement();
			insertStatement.execute(sqlStatement.toString());
		}
	}

	public void delete(Connection connection, A record) throws SQLException {
		String sql = "delete from " + tablename + " where id = " + record.getId() + ";";

		Statement statement = connection.createStatement();
		statement.execute(sql);
		statement.close();

		record.setId(null);
	}

	public A findById(Connection connection, Long id) throws SQLException {
		List<String> columnNames = new ArrayList<String>(columnMap.keySet());

		Statement statement = connection.createStatement();
		String sql = "select " + Joiner.on(", ").join(columnNames) + " from " + tablename + " where id =" + id
		+ " limit 1;";

		ResultSet resultSet = statement.executeQuery(sql);

		List<A> results = fromResultSet(connection, resultSet);

		if (results.size() == 1)
			return results.get(0);
		else
			return null;
	}

	/**
	 * Returns the results of a select query using the given parameters in the
	 * where clause, e.g.
	 * <code>SELECT * FROM Table WHERE fields.get(0) relations.get(0) values.get(0) && fields.get(1) �</code>
	 * where relations.get(0) is one of the binary relations specified in
	 * {@link Relation}. The results can be ordered optionally by setting
	 * "orderByFields" to a non null list of field values.
	 * 
	 * @param fields
	 * @param relations
	 * @param values
	 * @param orderByFields
	 * @param ascendingValues
	 * @return
	 * @throws SQLException
	 */
	public List<A> runQueryWithParameters(Connection connection, List<String> fields, List<Relation> relations,
			List<Object> values, List<Operator> operators, List<String> orderByFields, List<Boolean> ascendingValues,
			Integer limit) throws SQLException {
		List<String> columnNames = new ArrayList<String>(columnMap.keySet());

		for (String field : fields) {
			if (!columnMap.containsKey(javaToUnderscore(field)))
				throw new IllegalArgumentException("Field " + field + " is not a member of "
						+ mappedClass.getSimpleName());
		}

		for (String field : orderByFields) {
			if (!columnMap.containsKey(javaToUnderscore(field)))
				throw new IllegalArgumentException("Field " + field + " is not a member of "
						+ mappedClass.getSimpleName());
		}

		StringBuffer sqlStatement = new StringBuffer("Select ");

		sqlStatement.append(Joiner.on(",").join(columnNames));

		sqlStatement.append(" from ");
		sqlStatement.append(tablename);

		if (!fields.isEmpty()) {
			sqlStatement.append(" where ");
			for (int i = 0; i < fields.size(); i++) {
				sqlStatement.append(javaToUnderscore(fields.get(i)));
				sqlStatement.append(" ");
				sqlStatement.append(relations.get(i));
				sqlStatement.append(" ");
				sqlStatement.append(TypeMapper.postgresify(values.get(i)));

				if (i < fields.size() - 1) {
					sqlStatement.append(" ");
					sqlStatement.append(operators.get(i));
					sqlStatement.append(" ");
				}
			}
		}

		if (!orderByFields.isEmpty()) {
			sqlStatement.append(" order by ");
			for (int i = 0; i < orderByFields.size(); i++) {
				sqlStatement.append(javaToUnderscore(orderByFields.get(i)));
				if (ascendingValues.get(i))
					sqlStatement.append(" asc");
				else
					sqlStatement.append(" desc");
				if (i < orderByFields.size() - 1)
					sqlStatement.append(" ,");
			}
		}

		if (limit != null) {
			sqlStatement.append(" limit ");
			sqlStatement.append(limit);
		}

		sqlStatement.append(';');

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery(sqlStatement.toString());

		return fromResultSet(connection, resultSet);
	}

	public Set<A> obtainContentsOfRelation(Connection connection, String relationName, Long ownerId, boolean inverse)
	throws SQLException {
		StringBuffer sqlStatement = new StringBuffer();
		sqlStatement.append("select ");
		sqlStatement.append(Joiner.on(",").join(columnMap.keySet()));
		sqlStatement.append(" from ");
		sqlStatement.append(tablename);
		sqlStatement.append(" join ");
		sqlStatement.append(relationName);
		sqlStatement.append(" on ");
		sqlStatement.append(relationName);
		sqlStatement.append(".");

		if (inverse)
			sqlStatement.append(LEFT_COLUMN);
		else
			sqlStatement.append(RIGHT_COLUMN);

		sqlStatement.append(" = ");
		sqlStatement.append(tablename);
		sqlStatement.append(".id where ");

		sqlStatement.append(relationName);
		sqlStatement.append(".");
		if (inverse)
			sqlStatement.append(RIGHT_COLUMN);
		else
			sqlStatement.append(LEFT_COLUMN);
		sqlStatement.append(" = ");
		sqlStatement.append(ownerId);
		sqlStatement.append(";");

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sqlStatement.toString());
		return new HashSet<A>(fromResultSet(connection, resultSet));
	}

	private String tableNameForInverse(Field field) {
		String value = field.getAnnotation(Inverse.class).value();

		if (!value.matches("^(([a-zA-Z_])+\\.)*([A-Za-z])+\\.[A-Za-z0-9\\$]+$"))
			throw new IllegalArgumentException();

		String className = value.replaceAll("\\.[a-zA-Z0-9\\$]+$", "");
		String member = value.replaceAll("^(([a-zA-Z_])+\\.)+", "");
		assert (className + "." + member).equals(value);
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			Class<?> c = clazz;
			Field original = null;
			superclass: while (c != null) {
				for (Field f : c.getDeclaredFields()) {
					if (f.getName().equals(member)) {
						original = f;
						break superclass;
					}
				}
				c = c.getSuperclass();
			}
			if (original == null)
				throw new IllegalArgumentException("Member " + member + " does not exist in " + className);
			if (original.getAnnotation(Inverse.class) != null)
				throw new IllegalStateException("Inverse must not specify an inverse itself");

			ClassMapper<? extends ActiveRecord> clazzMapper = mapper
			.getClassMapperForClass((Class<? extends ActiveRecord>) clazz);
			return clazzMapper.oneToMany.inverse().get(original);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e); // XXX
		}
	}

	private boolean exists(Connection connection, String tableName) throws SQLException {
		String sql = "select count(*) from pg_tables where schemaname = 'public' and tablename = '" + tableName + "';";
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		boolean result = false;

		if (resultSet.next())
			result = resultSet.getInt(1) == 1;

		statement.close();
		return result;
	}

	/**
	 * Inserting an active record to the db that has not been saved before. The
	 * method does not take care of the one-to-many realtions of an active
	 * record.
	 * 
	 * @param connection
	 * @param record
	 * @throws SQLException
	 */
	private void insert(Connection connection, A record) throws SQLException {
		List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();

		record.setUpdatedAt(new Date());

		for (Entry<String, Field> entry : columnMap.entrySet()) {
			Field field = entry.getValue();

			if (field == idField)
				continue;

			columns.add(entry.getKey());

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(record);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}

			// handle one-to-one relations: if the active record has not saved
			// before, save it

			if (value != null) {
				if (ActiveRecord.class.isAssignableFrom(value.getClass())) {
					if (((ActiveRecord) value).getId() == null) {
						ClassMapper classMapper = mapper.getClassMapperForClass(((ActiveRecord) value).getClass());
						classMapper.save(connection, ((ActiveRecord) value));
					}
				}
			}

			values.add(TypeMapper.postgresify(value));
		}

		Statement statement = connection.createStatement();
		String sql = "insert into " + tablename + "(" + Joiner.on(", ").join(columns) + ") values ("
		+ Joiner.on(", ").join(values) + ") returning id;";

		ResultSet result = statement.executeQuery(sql);

		if (result.next()) {
			Long newId = result.getLong(1);
			record.setId(newId);
		} else {
			throw new IllegalStateException();
		}
		result.close();
		statement.close();
	}

	/**
	 * Updating a record in the db. The method does not take care of the
	 * one-to-many realtions of an active record.
	 * 
	 * @param connection
	 * @param record
	 * @throws SQLException
	 */
	private void update(Connection connection, A record) throws SQLException {
		Map<String, String> columnsAndValues = new HashMap<String, String>();

		record.setUpdatedAt(new Date());

		for (Entry<String, Field> entry : columnMap.entrySet()) {
			Field field = entry.getValue();

			if (field == idField)
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(record);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}

			// handle one-to-one relations: if the active record has not saved
			// before, save it

			if (value != null) {
				if (ActiveRecord.class.isAssignableFrom(value.getClass())) {
					if (((ActiveRecord) value).getId() == null) {
						ClassMapper classMapper = mapper.getClassMapperForClass(((ActiveRecord) value).getClass());
						classMapper.save(connection, ((ActiveRecord) value));
					}
				}
			}

			columnsAndValues.put(entry.getKey(), TypeMapper.postgresify(value));
		}

		Statement statement = connection.createStatement();

		String sql = "update " + tablename + " set "
		+ Joiner.on(", ").withKeyValueSeparator(" = ").join(columnsAndValues) + " where id =" + record.getId()
		+ ";";

		statement.execute(sql);
		statement.close();
	}

	private void assignEnumToField(A record, Class<?> type, Field field, String value) throws IllegalArgumentException,
	IllegalAccessException {
		// refactor this away
		if (value == null || value.equals("null")) {
			field.set(record, null);
			return;
		}

		for (Object e : type.getEnumConstants()) {
			Enum<?> en = (Enum<?>) e;
			if (en.name().equals(value)) {
				field.set(record, e);
				return;
			}
		}
		throw new IllegalStateException("Enum " + field.getType().getName() + " does not have a value " + value);
	}

	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<A> fromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		List<A> results = new ArrayList<A>();

		List<String> columnNames = Lists.newArrayList(columnMap.keySet());
		List<Field> fields = Lists.newArrayList(columnMap.values());

		while (resultSet.next()) {
			A record = null;
			try {
				record = mappedClass.newInstance();

				for (int i = 0; i < columnNames.size(); i++) {
					Field f = fields.get(i);
					Object v = resultSet.getObject(columnNames.get(i));
					f.setAccessible(true);
					Class<?> type = f.getType();

					if (type.isEnum()) {
						assignEnumToField(record, type, fields.get(i), (String) v);
					} else if (ActiveRecord.class.isAssignableFrom(type)) {
						// handle one-to-one relations
						long id = resultSet.getLong(columnNames.get(i));
						ClassMapper<? extends ActiveRecord> classMapper = mapper
						.getClassMapperForClass((Class<? extends ActiveRecord>) type);
						ActiveRecord activeRecord = classMapper.findById(connection, id);
						fields.get(i).set(record, activeRecord);
					} else {
						fields.get(i).set(record, resultSet.getObject(columnNames.get(i)));
					}
				}

				for (Entry<String, Field> entry : oneToMany.entrySet()) {
					Field f = entry.getValue();
					boolean inverse = f.isAnnotationPresent(Inverse.class);
					ParameterizedType set = (ParameterizedType) f.getGenericType();
					Class<? extends ActiveRecord> setType = (Class<? extends ActiveRecord>) set
					.getActualTypeArguments()[0];
					ClassMapper<? extends ActiveRecord> mapperForLazySet = mapper.getClassMapperForClass(setType);
					LazySet<?> lazySet = new LazySet<ActiveRecord>(mapper,
							(ClassMapper<ActiveRecord>) mapperForLazySet, entry.getKey(), record.getId(), inverse);

					f.setAccessible(true);
					f.set(record, lazySet);
				}

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			results.add(record);
		}

		return results;
	}

	private static String javaToUnderscore(String string) {
		StringBuffer result = new StringBuffer();

		boolean firstNumber = true;
		result.append(Character.toLowerCase(string.charAt(0)));
		for (int i = 1; i < string.length(); i++) {
			char c = string.charAt(i);

			if (Character.isUpperCase(c)) {
				result.append('_').append(Character.toLowerCase(c));
			} else if (firstNumber && Character.isDigit(c)) {
				result.append('_').append(c);
				firstNumber = false;
			} else {
				result.append(c);
				firstNumber = true;
			}
		}

		return result.toString();
	}
}
