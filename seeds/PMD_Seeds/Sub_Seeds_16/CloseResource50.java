
import java.io.*;

public class CloseResourceFP {
    public void check(File outputFile) {
        final OutputStream os;
        if (outputFile == null) {
            os = System.out;
        } else if (outputFile.isAbsolute()) {
            os = Files.newOutputStream(outputFile.toPath());
        } else {
            os = Files.newOutputStream(new File(getProject().getBaseDir(), outputFile.toString()).toPath());
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
            renderer.render(cpd.getMatches(), writer);
        }
    }
}
        