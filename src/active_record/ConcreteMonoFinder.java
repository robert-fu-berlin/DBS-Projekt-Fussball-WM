package active_record;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import active_record.finder.InitialMonoFinder;
import active_record.finder.MonoFinder;
import active_record.finder.MonoNeedsValue;

public class ConcreteMonoFinder<T extends ActiveRecord> implements
		InitialMonoFinder<T>, MonoFinder<T>, MonoNeedsValue<T> {

	private ActiveRecordMapper activeRecordMapper;
	private List<String>	fields;
	private List<Relation>	relations;
	private List<Object>	values;

	private ClassMapper<T>	classMapper;

	public ConcreteMonoFinder(ActiveRecordMapper activeRecordMapper, ClassMapper<T> classMapper) {
		this.classMapper = classMapper;
		this.activeRecordMapper = activeRecordMapper;

		fields = new ArrayList<String>();
		relations = new ArrayList<Relation>();
		values = new ArrayList<Object>();
	}

	@Override
	public MonoNeedsValue<T> where(String field) {
		fields.add(field);
		return this;
	}

	@Override
	public MonoFinder<T> is(Object value) {
		relations.add(Relation.EQUALS);
		values.add(value);
		return this;
		
	}

	@Override
	public T please() {
		if (!(fields.size() == relations.size() && relations.size() == values.size()))
			throw new IllegalStateException("Methods have been called in illegal order"); // XXX find better wording
		
		try {
			Connection connection = activeRecordMapper.obtainConnection();
			List<T> results = classMapper.runQueryWithParameters(connection, fields, relations, values);
			T result = results.size() > 0 ? results.get(0) : null;
			
			connection.commit();
			connection.close();
			return result;
		} catch (SQLException e) {
			throw new IllegalStateException(e); // TODO find better exception, consider checked FinderException
		}
	} 

}
