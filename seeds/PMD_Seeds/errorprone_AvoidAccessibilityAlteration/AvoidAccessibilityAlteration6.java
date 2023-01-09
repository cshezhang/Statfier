
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

public class Violation {
    private void invalidSetAccessCalls() throws NoSuchMethodException, SecurityException {
        Constructor<?> constructor = this.getClass().getDeclaredConstructor(String.class);
        // call to setAccessible with false - that's ok
        constructor.setAccessible(false);

        Constructor<?>[] constructors = this.getClass().getConstructors();
        AccessibleObject.setAccessible(constructors, false);
        Constructor.setAccessible(constructors, false);
    }
}
        