package active_record;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class LazySet<T extends ActiveRecord> implements Set<T> {

	private Set<T> buffer;
	private final ActiveRecordMapper arm;
	private final String tableName;
	private final Long ownerId;
	private final boolean inverse;
	private final ClassMapper<T> classMapper;

	public LazySet(ActiveRecordMapper arm, ClassMapper<T> classMapper, String tableName, Long ownerId, boolean inverse) {
		this.arm = arm;
		this.tableName = tableName;
		this.ownerId = ownerId;
		this.inverse = inverse;
		this.classMapper = classMapper;
	}

	private void populateBuffer() {
		if (buffer != null)
			return;
		Connection connection = null;
		try {
			connection = arm.obtainConnection();
			buffer = classMapper.obtainContentsOfRelation(connection, tableName, ownerId, inverse);
		} catch (SQLException e) {
			//TODO think about that
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Set<?>))
			return false;

		Set<?> other = (Set<?>) obj;

		populateBuffer();

		if (this.size() != other.size())
			return false;

		for (T t : buffer)
			if (!other.contains(t))
				return false;

		return true;
	}

	@Override
	public int hashCode() {
		return tableName.hashCode() ^ ownerId.hashCode();
	}

	@Override
	public boolean add(T arg) {
		populateBuffer();
		return buffer.add(arg);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg) {
		populateBuffer();
		return buffer.addAll(arg);
	}

	@Override
	public void clear() {
		populateBuffer();
		buffer.clear();
	}

	@Override
	public boolean contains(Object arg) {
		populateBuffer();
		return buffer.contains(arg);
	}

	@Override
	public boolean containsAll(Collection<?> arg) {
		populateBuffer();
		return buffer.containsAll(arg);
	}

	@Override
	public boolean isEmpty() {
		populateBuffer();
		return buffer.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		populateBuffer();
		return buffer.iterator();
	}

	@Override
	public boolean remove(Object arg) {
		populateBuffer();
		return buffer.remove(arg);
	}

	@Override
	public boolean removeAll(Collection<?> arg) {
		populateBuffer();
		return buffer.removeAll(arg);
	}

	@Override
	public boolean retainAll(Collection<?> arg) {
		populateBuffer();
		return buffer.retainAll(arg);
	}

	@Override
	public int size() {
		populateBuffer();
		return buffer.size();
	}

	@Override
	public Object[] toArray() {
		populateBuffer();
		return buffer.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg) {
		populateBuffer();
		return buffer.toArray(arg);
	}
}
