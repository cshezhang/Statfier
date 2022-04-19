



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LeaksAccessPaths {

  void acquireTwoThenReleaseOneTwiceBad() throws IOException, FileNotFoundException {
    FileInputStream stream1 = new FileInputStream("file1.txt");
    FileInputStream stream2 = new FileInputStream("file2.txt");
    stream1.close();
    stream1.close();
  }
}
