package active_record;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of the Set Interface used for lazy retrieval of
 * {@link ActiveRecord} instances.
 * 
 * @author Robert B�hnke
 * @author Elena Weihe
 */
public class LazySet<T extends ActiveRecord> implements Set<T> {

	private Long ownerId;
	private String tablename;
	private String contentClass;
	private ClassMapper<T> activeTable;
	private Set<T> cache;

	private boolean filledCache = false;

	public LazySet(ClassMapper<T> activeTable, Long ownerId, String tablename,
			String contentClass) {
		this.activeTable = activeTable;
		this.ownerId = ownerId;
		this.tablename = tablename;
		this.contentClass = contentClass;
		this.cache = new HashSet<T>();
	}

	/**
	 * Obtains the Active Records from the database and stores them in the cache
	 * set.
	 */
	private void fillCache() {
		// aus der Datenbank??

	}

	@Override
	public boolean add(T e) {
		if (!filledCache)
			fillCache();

		return cache.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (!filledCache)
			fillCache();

		return cache.addAll(c);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public boolean contains(Object o) {
		if (!filledCache)
			fillCache();

		return cache.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (!filledCache)
			fillCache();

		return cache.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		if (!filledCache)
			fillCache();

		return cache.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		if (!filledCache)
			fillCache();

		return cache.iterator();
	}

	@Override
	public boolean remove(Object o) {
		if (!filledCache)
			fillCache();

		return cache.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (!filledCache)
			fillCache();

		return cache.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (!filledCache)
			fillCache();

		return cache.retainAll(c);
	}

	@Override
	public int size() {
		if (!filledCache)
			fillCache();

		return cache.size();
	}

	@Override
	public Object[] toArray() {
		if (!filledCache)
			fillCache();

		return cache.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (!filledCache)
			fillCache();

		return cache.toArray(a);
	}

}
