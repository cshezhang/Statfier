
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Violation {
    public void invalidSetAccessibleCalls() {
        for (Constructor<?> constructor : this.getClass().getConstructors()) {
            constructor.setAccessible(true);
        }
        Constructor<?>[] constructors = this.getClass().getConstructors();
        AccessibleObject.setAccessible(constructors, true);
        Constructor.setAccessible(constructors, true);

        for (Method method : this.getClass().getMethods()) {
            method.setAccessible(true);
        }
        Method[] methods = this.getClass().getMethods();
        AccessibleObject.setAccessible(methods, true);
        Method.setAccessible(methods, true);

        for (Field field : this.getClass().getFields()) {
            field.setAccessible(true);
        }
        Field[] fields = this.getClass().getFields();
        AccessibleObject.setAccessible(fields, true);
        Field.setAccessible(fields, true);
    }
}
        