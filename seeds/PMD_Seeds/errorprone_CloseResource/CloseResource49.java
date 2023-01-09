
import java.io.*;

public class CloseResourceFP {
    public void check(InputStream in) {
        if (in instanceof FileInputStream) {
            FileInputStream fin = (FileInputStream) in;
            doCheck(fin);
        } else if (in instanceof ByteArrayInputStream) {
            ByteArrayInputStream bin = (ByteArrayInputSream) in;
            doCheck(bin);
        } else {
            BufferedInputStream buf = new BufferedInputStream(in);
            doCheck(buf);
        }
    }

    public void dump(final Writer writer) {
        final PrintWriter printWriter = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
        printWriter.println(this);
    }
}
        