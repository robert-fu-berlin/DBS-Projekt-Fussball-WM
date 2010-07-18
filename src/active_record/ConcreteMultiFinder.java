package active_record;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import active_record.finder.MultiFinder;
import active_record.finder.MultiNeedsValue;

public class ConcreteMultiFinder<T extends ActiveRecord> implements
		MultiFinder<T>, MultiNeedsValue<T> {

	private ActiveRecordMapper activeRecordMapper;
	private List<String>	fields;
	private List<Relation>	relations;
	private List<Object>	values;
	private List<Boolean> ascendingValues;
	private List<String> orderByFields;

	private ClassMapper<T>	classMapper;

	public ConcreteMultiFinder(ActiveRecordMapper activeRecordMapper, ClassMapper<T> classMapper) {
		this.classMapper = classMapper;
		this.activeRecordMapper = activeRecordMapper;

		fields = new ArrayList<String>();
		relations = new ArrayList<Relation>();
		values = new ArrayList<Object>();
		ascendingValues = new ArrayList<Boolean>();
		orderByFields = new ArrayList<String>();
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
		
		try {
			Connection connection = activeRecordMapper.obtainConnection();
			List<T> results = classMapper.runQueryWithParameters(connection, fields, relations, values, orderByFields, ascendingValues);
			
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

}
