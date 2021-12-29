
import java.io.*;

public class CloseResourcePrintWriter {
    public String run1() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("Foo");
        String result = sw.toString();
        return result;
    }

    public String run2() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        pw.println("Foo");
        String result = sw.toString();
        return result;
    }

    public String run3() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(new BufferedWriter(sw));

        pw.println("Foo");
        return sw.toString();
    }
}
        