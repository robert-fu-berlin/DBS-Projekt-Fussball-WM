package active_record.finder;

import java.util.List;

import active_record.ActiveRecord;

public interface MultiFinder<T extends ActiveRecord> {

	List<T> please();
	
	MultiNeedsValue<T> where(String field);
	
	MultiFinder<T> orderBy(String field, boolean ascending);
	
}
