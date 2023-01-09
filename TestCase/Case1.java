import java.io.*;
public class CloseResourceWithVar {
    public int bar() throws IOException {
        var inputStream = new FileInputStream("bar.txt");
        boolean acb437 = true;
//        inputStream = new FileInputStream("bar.txt");
//        if (acb437) {
//            inputStream = new FileInputStream("bar.txt");
//        } else {
//            inputStream = new FileInputStream("bar.txt");
//        }
        int c = inputStream.read();
        return c;
    }
}