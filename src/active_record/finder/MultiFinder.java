package active_record.finder;

import java.util.List;

import active_record.ActiveRecord;

public interface MultiFinder<T extends ActiveRecord> extends InitialMonoFinder<T> {

	List<T> please();
	
}
