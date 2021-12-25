
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import net.sourceforge.pmd.lang.java.rule.errorprone.closeresource.CustomStringWriter;

public class CloseResourceWithExceptions {
    public void bar() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(new byte[] {1, 2, 3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] result = buffer.toByteArray();
        return result;
    }

    public String baz() {
        StringWriter writer = new CustomStringWriter();
        writer.write("Test");
        return writer.toString();
    }
}
        