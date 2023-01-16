



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tell the analysis that a collection is thread-safe when this information is not already reflected
 * in the collection's type. For example: private {@literal @SynchronizedCollection} Map mMap =
 * Collections.synchronizedMap(...);
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface SynchronizedCollection {}
