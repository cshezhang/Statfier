
import java.lang.reflect.Constructor;
import java.util.List;

public class NoViolation { {
    List<Constructor<?>> list = new ArrayList<>();
    Constructor<?> ctor = NoViolation.class.getConstructor();
    list.add(ctor);
} }
        