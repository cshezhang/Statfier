
import java.io.*;
public class BadClose {
    private void readData() {
        File aFile = new File(FileName);
        FileInputStream anInput = new FileInputStream(aFile);
        ObjectInputStream aStream = new ObjectInputStream(anInput);

        readExternal(aStream);
    }
}
        