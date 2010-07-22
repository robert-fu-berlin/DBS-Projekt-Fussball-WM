package active_record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.primitives.Primitives;

/**
 * This class maps Java primitive types to <i>Postgres</i> types and back. The JDBC Driver
 * has its own tool for this but this allows for a more fine-grained control on our end and
 * it also supports our implementation of {@link ActiveRecord}.
 * 
 * @author Robert B�hnke
 *
 */
class TypeMapper {

	private static final ImmutableMap<Class<?>, String> javaToPostgres;
	private static final ImmutableMap<String, Class<?>> postgresToJava;
	private static final DateFormat formatter = new SimpleDateFormat("yyyy-mm-DD");

	static {
		Builder<Class<?>,String> builder1 = new ImmutableMap.Builder<Class<?>, String>();
		builder1.put(Boolean.class, "boolean");
		builder1.put(Short.class,   "smallint");
		builder1.put(Integer.class, "integer");
		builder1.put(Long.class,    "bigint");
		builder1.put(Float.class,   "real");
		builder1.put(Double.class,  "double precision");
		builder1.put(String.class,  "text");
		builder1.put(Date.class,	"date");
		javaToPostgres = builder1.build();

		Builder<String, Class<?>> builder2 = new ImmutableMap.Builder<String, Class<?>>();

		// Add the inverse
		for (Class<?> javaType : javaToPostgres.keySet())
			builder2.put(javaToPostgres.get(javaType), javaType);

		// Add the aliases
		builder2.put("float4", Float.class);
		builder2.put("float8", Double.class);
		builder2.put("int2",   Short.class);
		builder2.put("int",    Integer.class);
		builder2.put("int4",   Integer.class);
		builder2.put("int8",   Long.class);

		postgresToJava = builder2.build();
	}

	public static boolean hasMapping(Class<?> type) {
		return ActiveRecord.class.isAssignableFrom(type) || javaToPostgres.containsKey(type);
	}

	public static String postgresForJava(Class<?> javaType) {
		if (ActiveRecord.class.isAssignableFrom(javaType))
			return javaToPostgres.get(Long.class);
		else if (javaType.isPrimitive())
			return javaToPostgres.get(Primitives.wrap(javaType));
		else if (javaType.isEnum()) {
			return "text";	//XXX
		}
		else
			return javaToPostgres.get(javaType);
	}

	public static Class<?> javaForPostgres(String string) {
		return postgresToJava.get(string);
	}

	public static String postgresify(Object value) {
		if (value == null)
			return "null";

		if (value.getClass().isEnum())
			return '\'' + ((Enum)value).name() + '\'';

		if (ActiveRecord.class.isAssignableFrom(value.getClass()))
			return ((ActiveRecord) value).getId().toString();

		if (postgresForJava(value.getClass()) == null)
			throw new IllegalArgumentException("Cannot map objects of type " + value.getClass());

		if (value.getClass() == String.class)
			return "\'" + value + "\'";

		if (value.getClass() == Date.class)
			return "\'" + formatter.format((Date) value) + "\'";

		return value.toString();
	}
}
