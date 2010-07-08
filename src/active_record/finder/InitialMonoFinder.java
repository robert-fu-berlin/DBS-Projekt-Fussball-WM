package active_record.finder;

import active_record.ActiveRecord;

public interface InitialMonoFinder<T extends ActiveRecord> {

	MonoNeedsValue<T> where(String field);
	
}
