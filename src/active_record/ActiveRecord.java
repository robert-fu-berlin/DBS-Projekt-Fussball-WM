package active_record;

import java.util.Date;

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
	
}
