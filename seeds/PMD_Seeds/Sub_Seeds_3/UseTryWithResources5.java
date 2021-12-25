
import java.io.*;

public class TryWithResources {
    public void run() {
        Reader r1 = new FileReader(file);
        try (Reader r2 = new FileReader(otherFile)) {
        } finally {
            r1.close();
        }
    }
}
        