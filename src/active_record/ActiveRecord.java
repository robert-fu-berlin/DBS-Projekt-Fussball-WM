package active_record;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class ActiveRecord {

	protected Long id = null;
	protected Long createdAt = System.currentTimeMillis();
	protected Long updatedAt = new Long(createdAt);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return new Date(createdAt);
	}

	public void setUpdatedAt(Date date) {
		this.updatedAt = date.getTime();
	}

	public Date getUpdatedAt() {
		return new Date(updatedAt);
	}

	/**
	 * ActiveRecord is considered valid if an empty list is returned
	 */
	public List<ValidationFailure> validate() {return Collections.EMPTY_LIST;}

	public List<ValidationFailure> validateAssociated() {return Collections.EMPTY_LIST;}
}
