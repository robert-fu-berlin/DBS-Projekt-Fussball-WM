package active_record.finder;

import active_record.ActiveRecord;

public interface InitialMultiFinder<T extends ActiveRecord> {

	MultiNeedsValue<T> where(String field);
	
}
