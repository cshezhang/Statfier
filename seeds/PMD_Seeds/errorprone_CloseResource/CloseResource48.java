
import java.io.*;
import java.util.Scanner;

public class CloseResourceCase {
    public void run() {
        try {
            FileInputStream in = new FileInputStream("MyFile.txt");
            Scanner input = new Scanner(System.in, "utf-8");
            String file = "MyFile.txt";
            FileInputStream in2 = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
        