package active_record.finder;

import active_record.ActiveRecord;

public interface MonoNeedsValue<T extends ActiveRecord> {

	MonoFinder<T> is(Object value);
	
}
