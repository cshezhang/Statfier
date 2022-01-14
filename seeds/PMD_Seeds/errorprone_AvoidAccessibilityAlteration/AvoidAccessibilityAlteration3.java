
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class NoViolation {
    public void explicitAccessibilityAlteration() {
        Method[] methods = AccessController.doPrivileged(new PrivilegedAction<Method[]>() {
            @Override
            public Method[] run() {
                Method[] declaredMethods = Violation.class.getDeclaredMethods();
                AccessibleObject.setAccessible(declaredMethods, true);
                return declaredMethods;
            }
        });
        try {
            methods[0].invoke(null);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
        