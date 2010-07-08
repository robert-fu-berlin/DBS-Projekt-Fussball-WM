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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableBiMap.Builder;

class ClassMapper<A extends ActiveRecord> {

	private final String tablename;
	
	private final ImmutableBiMap<String, Field> columnMap;
	
	private final ImmutableBiMap<String, Field> oneToMany;
	
	private final ActiveRecordMapper mapper;
	
	private Field idField;
	
	private final Class<A> mappedClass;
	
	public ClassMapper(Class<A> activeRecord, ActiveRecordMapper mapper, String prefix) {
		this.tablename = (prefix != null ? prefix + "_" : "") + javaToUnderscore(activeRecord.getSimpleName());
		
		// Obtain a list of all fields the class has access to.
		List<Field> fields = new ArrayList<Field>();
		List<Field> sets = new ArrayList<Field>();
		
		Class<?> c = activeRecord;
		while (c != null) {
			Collections.addAll(fields, c.getDeclaredFields());
			c = c.getSuperclass();
		}
		
		// Filter static and synthetic fields, ensure valid types, add sets as one to many relationships
		java.util.Iterator<Field> iterator = fields.iterator();
		while(iterator.hasNext()) {
			Field f = iterator.next();
			Class<?> type = f.getType();
			
			if (f.isSynthetic() || (f.getModifiers() & Modifier.STATIC) != 0) {
				iterator.remove();
				continue;
			}
			
			if (TypeMapper.hasMapping(type))
				continue;
			
			if (type.isAssignableFrom(Set.class)) {
				ParameterizedType set = (ParameterizedType) f.getGenericType();
				
				Class<?> setType = (Class<?>) set.getActualTypeArguments()[0];
				
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
			oneToManyBuilder.put(javaToUnderscore(field.getName()), field);
		}
		
		this.oneToMany = oneToManyBuilder.build();
		
		mappedClass = activeRecord;
		this.mapper = mapper;
	}
	
	public void createTable(Connection connection) throws SQLException {
		if (connection.isClosed())
			throw new IllegalArgumentException("Connection " + connection + "is closed.");
		
		Map<String, String> columnsAndSqlTypes = new HashMap<String, String>(columnMap.size());
		
		// Map java types to their corresponding postgres types
		for (Entry<String, Field> entry : columnMap.entrySet()) {
			if (entry.getValue() == idField) {
				columnsAndSqlTypes.put(entry.getKey(), "serial8 primary key");
				continue;
			}
			
			String column = entry.getKey();
			String type = TypeMapper.postgresForJava(entry.getValue().getType());
			
			System.out.println(column + " : " + type);
			
			columnsAndSqlTypes.put(column, type);
		}
		
		String sql = "create table " + tablename + " (" + Joiner.on(", ").withKeyValueSeparator(" ").join(columnsAndSqlTypes) + ");";
		
		System.out.println(sql);
		
		Statement statement = connection.createStatement();
		statement.execute(sql);
		statement.close();
		
		// XXX Find better name for columns, add foreign key constraints
		for (Entry<String, Field> entry : oneToMany.entrySet()) {
			sql = "create table " + (tablename + '_' + entry.getKey() ) + "(one bigint, many " + TypeMapper.postgresForJava((Class<?>) ((ParameterizedType) entry.getValue().getGenericType()).getActualTypeArguments()[0]) + ");";
			
			System.out.println(sql);
			
			statement = connection.createStatement();
			statement.execute(sql);
			statement.close();
		}
	}
	
	public void save(Connection connection, A record) throws SQLException {
		// TODO Validate
		
		if (record.getId() == null)
			insert(connection, record);
		else
			update(connection, record);
	}
	
	/**
	 * TODO Persist objects in sets (one to many relations) 
	 * 
	 * @param connection
	 * @param record
	 * @throws SQLException
	 */
	private void insert(Connection connection, A record) throws SQLException {
		List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
		
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

			values.add(TypeMapper.postgresify(value));
		}
		
		Statement statement = connection.createStatement();
		String sql = "insert into " + tablename + "(" + Joiner.on(", ").join(columns) + ") values (" + Joiner.on(", ").join(values) + ") returning id;";
		
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
	 * TODO Persist objects in sets (one to many relations)
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

			columnsAndValues.put(entry.getKey(), TypeMapper.postgresify(value));
		}
		
		Statement statement = connection.createStatement();
		
		String sql = "update " + tablename + " set " + Joiner.on(", ").withKeyValueSeparator(" = ").join(columnsAndValues) + " where id =" + record.getId() + ";";
		
		System.out.println(sql);
		
		statement.execute(sql);
		statement.close();
	}

	public A findById(Connection connection, Long id) throws SQLException {
		List<String> columnNames = new ArrayList<String>(columnMap.size());
		List<Field> fields = new ArrayList<Field>(columnMap.size());
		
		for (Entry<String, Field> entry : columnMap.entrySet()) {
			columnNames.add(entry.getKey());
			fields.add(entry.getValue());
		}
		
		Statement statement = connection.createStatement();
		String sql = "select " + Joiner.on(", ").join(columnNames) + " from " + tablename + " where id =" + id + " limit 1;";
		
		ResultSet resultSet = statement.executeQuery(sql);
		
		List<A> results = fromResultSet(resultSet);
		
		if (results.size() == 1)
			return results.get(0);
		else
			return null;
	}
	
	/**
	 * TODO Load objects in sets (one to many relations)
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<A> fromResultSet(ResultSet resultSet) throws SQLException {
		List<A> results = new ArrayList<A>();
		
		List<String> columnNames = Lists.newArrayList(columnMap.keySet());
		List<Field> fields = Lists.newArrayList(columnMap.values());
		
		while (resultSet.next()) {
			A record = null;
			try {
				record =  mappedClass.newInstance();
				
				for (int i = 0; i < columnNames.size(); i++) {
					fields.get(i).setAccessible(true);
					fields.get(i).set(record, resultSet.getObject(columnNames.get(i)));
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
