package active_record;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of the Set Interface used for lazy retrieval of
 * {@link ActiveRecord} instances from the relation tables.
 * 
 * @author Robert B�hnke
 * @author Elena Weihe
 */
public class LazySet<T extends ActiveRecord> implements Set<T> {

	/**
	 * primary key of the record the set belongs to ("one")
	 */
	private Long ownerId;
	/**
	 * name of the relation table the set represents ("many")
	 */
	private String tablename;
	/**
	 * mapper of the {@link ActiveRecord} that is stored in the set
	 */
	private ClassMapper<T> activeTable;
	/**
	 * set to store the {@link ActiveRecord ActiveRecords}
	 */
	private Set<T> cache;

	/**
	 * 
	 * @param activeTable mapper of the {@link ActiveRecord} that is stored in the set
	 * @param ownerId key of the record the set belongs to ("one")
	 * @param tablename name of the relation table the set represents ("many")
	 */
	public LazySet(ClassMapper<T> activeTable, Long ownerId, String tablename) {
		this.activeTable = activeTable;
		this.ownerId = ownerId;
		this.tablename = tablename;
	}

	/**
	 * Obtains the Active Records from the database and stores them in the cache
	 * set.
	 */
	private void fillCache() {
		try {
			cache = new HashSet<T>(activeTable.getSet(tablename, ownerId));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean add(T e) {
		if (cache == null)
			fillCache();

		return cache.add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		if (cache == null)
			fillCache();

		return cache.addAll(c);
	}

	public void clear() {
		cache.clear();
	}

	public boolean contains(Object o) {
		if (cache == null)
			fillCache();

		return cache.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		if (cache == null)
			fillCache();

		return cache.containsAll(c);
	}

	public boolean isEmpty() {
		if (cache == null)
			fillCache();

		return cache.isEmpty();
	}

	public Iterator<T> iterator() {
		if (cache == null)
			fillCache();

		return cache.iterator();
	}

	public boolean remove(Object o) {
		if (cache == null)
			fillCache();

		return cache.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		if (cache == null)
			fillCache();

		return cache.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		if (cache == null)
			fillCache();

		return cache.retainAll(c);
	}

	public int size() {
		if (cache == null)
			fillCache();

		return cache.size();
	}

	public Object[] toArray() {
		if (cache == null)
			fillCache();

		return cache.toArray();
	}

	public <T> T[] toArray(T[] a) {
		if (cache == null)
			fillCache();

		return cache.toArray(a);
	}

}
