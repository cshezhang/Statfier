
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class Violation {
    private void invalidSetAccessCalls() throws NoSuchMethodException, SecurityException {
        Constructor<?> constructor = this.getClass().getDeclaredConstructor(String.class);
        // call to forbidden setAccessible
        constructor.setAccessible(true);

        Method privateMethod = this.getClass().getDeclaredMethod("aPrivateMethod");
        // call to forbidden setAccessible
        privateMethod.setAccessible(true);

        // deliberate accessibility alteration
        String privateField = AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                try {
                    Field field = Violation.class.getDeclaredField("aPrivateField");
                    field.setAccessible(true);
                    return (String) field.get(null);
                } catch (ReflectiveOperationException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
        