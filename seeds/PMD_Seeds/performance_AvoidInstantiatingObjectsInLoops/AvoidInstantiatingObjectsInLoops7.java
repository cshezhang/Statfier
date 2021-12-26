
import java.util.ArrayList;
import java.io.File;

public class TestInstantiationInLoop {
    public static void main(String args[]) {
        for (String test : new ArrayList<String>()) { // facetious but simple example
            System.out.println(test);
        }
        for (String filename : new File("subdirectory").list()) { // complex but realistically conceivable (albeit exagerrated/oversimplified/rarely advisable) example
            System.out.println(filename);
        }
    }
}
        