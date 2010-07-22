package active_record;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import active_record.finder.InitialMultiFinder;
import active_record.finder.MultiFinder;
import active_record.finder.MultiNeedsValue;

public class ConcreteMultiFinder<T extends ActiveRecord> implements
InitialMultiFinder<T>, MultiFinder<T>, MultiNeedsValue<T> {

	private final ActiveRecordMapper activeRecordMapper;

	private final List<String>		fields;
	private final List<Relation>	relations;
	private final List<Object>		values;

	private final List<Boolean> 	ascendingValues;
	private final List<String> 		orderByFields;

	private final List<Operator>	operators;

	private Integer					limit;

	private final ClassMapper<T>	classMapper;

	public ConcreteMultiFinder(ActiveRecordMapper activeRecordMapper, ClassMapper<T> classMapper) {
		this.classMapper = classMapper;
		this.activeRecordMapper = activeRecordMapper;

		fields = new ArrayList<String>();
		relations = new ArrayList<Relation>();
		values = new ArrayList<Object>();
		ascendingValues = new ArrayList<Boolean>();
		orderByFields = new ArrayList<String>();
		operators = new ArrayList<Operator>();
	}

	@Override
	public MultiFinder<T> is(Object value) {
		relations.add(Relation.EQUALS);
		values.add(value);
		return this;

	}

	@Override
	public List<T> please() {
		if (!(fields.size() == relations.size() && relations.size() == values.size()))
			throw new IllegalStateException("Methods have been called in illegal order"); // XXX find better wording
		if (!(fields.isEmpty() && operators.isEmpty()) && !(fields.size() == operators.size() + 1))
			throw new IllegalStateException("Methods have been called in illegal order"); // XXX

		try {
			Connection connection = activeRecordMapper.obtainConnection();
			List<T> results = classMapper.runQueryWithParameters(connection, fields, relations, values, operators, orderByFields, ascendingValues, limit);

			connection.commit();
			connection.close();

			return results;
		} catch (SQLException e) {
			throw new IllegalStateException(e); // TODO find better exception, consider checked FinderException
		}
	}

	@Override
	public MultiFinder<T> orderBy(String field, boolean ascending) {
		orderByFields.add(field);
		ascendingValues.add(ascending);
		return this;
	}

	@Override
	public MultiNeedsValue<T> where(String field) {
		fields.add(field);
		return this;
	}

	@Override
	public List<T> limit(int limit) {
		this.limit = limit;
		return please();
	}

	@Override
	public MultiFinder<T> orderBy(String field) {
		return orderBy(field, true);
	}

	@Override
	public MultiNeedsValue<T> and(String field) {
		fields.add(field);
		operators.add(Operator.AND);
		return this;
	}

	@Override
	public MultiNeedsValue<T> or(String field) {
		fields.add(field);
		operators.add(Operator.OR);
		return this;
	}

	@Override
	public MultiFinder<T> isNot(Object value) {
		relations.add(Relation.UNEQUALS);
		values.add(value);
		return this;
	}

}
