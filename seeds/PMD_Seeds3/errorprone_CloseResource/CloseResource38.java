
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.OutputStream;

public class CloseResourceWithExceptions {
    public void bar() {
        /*ByteArray*/OutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(new byte[] {1, 2, 3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] result = buffer.toByteArray();
        return result;
    }

    public String baz() {
        StringWriter writer = new StringWriter();
        writer.write("Test");
        return writer.toString();
    }
}
        