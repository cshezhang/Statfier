
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.security.PrivilegedAction;

public class Violation {
    private static class MyPrivilegedAction implements PrivilegedAction<Field[]> {
        @Override
        public Field[] run() {
            Field[] declaredFields = Violation.class.getDeclaredFields();
            AccessibleObject.setAccessible(declaredFields, true);
            return declaredFields;
        }
    }
}
        