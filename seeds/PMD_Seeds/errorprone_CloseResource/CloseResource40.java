
import java.io.Writer;
import java.io.CharArrayWriter;
import java.io.IOException;

public class CloseResourceWithExceptions {

    public char[] bar() {
        /*CharArray*/Writer buffer = new CharArrayWriter();
        try {
            buffer.append("foo");
            return buffer.toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
        