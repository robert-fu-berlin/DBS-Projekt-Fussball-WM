package active_record;

import java.util.ArrayList;
import java.util.List;

import active_record.finder.InitialMonoFinder;
import active_record.finder.MonoFinder;
import active_record.finder.MonoNeedsValue;

public class ConcreteMonoFinder<T extends ActiveRecord> implements
		InitialMonoFinder<T>, MonoFinder<T>, MonoNeedsValue<T> {

	private List<String>	fields;
	private List<Relation>	relations;
	private List<Object>	values;

	private ClassMapper<T>	activeRecord;

	public ConcreteMonoFinder(ClassMapper<T> activeRecord) {
		this.activeRecord = activeRecord;

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
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < fields.size(); i++) {
			builder.append(fields.get(i));
			builder.append(" ");
			builder.append(relations.get(i).toString());
			builder.append(" ");
			builder.append(TypeMapper.postgresify(values.get(i)));
		}
		
		System.out.println(builder.toString());
		
		// TODO Run SQL statement, parse and return results
		
		return null;
	} 

}
