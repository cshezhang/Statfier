import java.io.Serializable;
import java.lang.reflect.Type;

public interface TypeProvider extends Serializable {
    Type getType();

    Object getSource();
}
