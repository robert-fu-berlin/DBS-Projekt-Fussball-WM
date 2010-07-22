package active_record.finder;

import active_record.ActiveRecord;

public interface MultiNeedsValue<T extends ActiveRecord> {

	MultiFinder<T> is(Object value);

	MultiFinder<T> isNot(Object value);

}
