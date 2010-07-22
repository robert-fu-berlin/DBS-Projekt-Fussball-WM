package active_record.finder;

import active_record.ActiveRecord;

public interface MonoFinder<T extends ActiveRecord> {

	T please();

	MonoNeedsValue<T> and(String field);

	MonoNeedsValue<T> or(String field);

}
