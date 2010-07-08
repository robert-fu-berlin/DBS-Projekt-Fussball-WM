package active_record.finder;

import active_record.ActiveRecord;

public interface MonoFinder<T extends ActiveRecord> extends InitialMonoFinder<T> {

	T please();
	
}
