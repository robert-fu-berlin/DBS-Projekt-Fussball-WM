package active_record;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value = FIELD)
@Retention(value = RUNTIME)
public @interface Inverse {

	//field that specifies inverse view direction
	String value();

}
