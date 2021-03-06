package active_record.finder;

import java.util.List;

import active_record.ActiveRecord;

public interface MultiFinder<T extends ActiveRecord> {

	List<T> please();

	List<T> limit(int limit);

	MultiFinder<T> orderBy(String field);

	MultiFinder<T> orderBy(String field, boolean ascending);

	MultiNeedsValue<T> and(String field);

	MultiNeedsValue<T> or(String field);

}
