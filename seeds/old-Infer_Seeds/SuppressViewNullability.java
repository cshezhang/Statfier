



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * View can be annotated with @SuppressViewNullability to silence warnings when a view is set to
 * null in a destructor, and created in an initializer.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface SuppressViewNullability {}
