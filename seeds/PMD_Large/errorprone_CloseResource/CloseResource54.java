
import java.io.*;
public class CloseResourceNullPointer {
    public void check(UnknownType param) {
        InputStream in = param;
    }
}
        