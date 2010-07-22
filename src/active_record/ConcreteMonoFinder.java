package active_record;

import java.util.List;

import active_record.finder.InitialMonoFinder;
import active_record.finder.MonoFinder;
import active_record.finder.MonoNeedsValue;

public class ConcreteMonoFinder<T extends ActiveRecord> implements
InitialMonoFinder<T>, MonoFinder<T>, MonoNeedsValue<T> {

	private final ConcreteMultiFinder<T> multiFinder;

	public ConcreteMonoFinder(ActiveRecordMapper activeRecordMapper, ClassMapper<T> classMapper) {
		multiFinder = new ConcreteMultiFinder<T>(activeRecordMapper, classMapper);
	}

	@Override
	public MonoNeedsValue<T> where(String field) {
		multiFinder.where(field);
		return this;
	}

	@Override
	public MonoNeedsValue<T> and(String field) {
		multiFinder.and(field);
		return this;
	}

	@Override
	public MonoNeedsValue<T> or(String field) {
		multiFinder.or(field);
		return this;
	}

	@Override
	public T please() {
		List<T> result = multiFinder.limit(1);
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

	@Override
	public MonoFinder<T> is(Object value) {
		multiFinder.is(value);
		return this;
	}

	@Override
	public MonoFinder<T> isNot(Object value) {
		multiFinder.isNot(value);
		return this;
	}

}
