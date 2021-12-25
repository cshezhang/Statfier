
public class Test {

    public void test() {
        final File[] files = new File(".").listFiles();
        for (final File f : files) { f.getAbsolutePath(); }
    }
}
        