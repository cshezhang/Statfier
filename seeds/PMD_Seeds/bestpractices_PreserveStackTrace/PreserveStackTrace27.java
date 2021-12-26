
public class Foo {
    public void foo(String a) throws Exception {
        try {
            int i = Integer.parseInt(a);
        } catch(java.io.FileNotFoundException e) {
            throw new Exception("file not found:" + e.toString());
        } catch(java.io.IOException e) {
            throw new Exception("I/O error:" + e);
        } catch(Exception e) {
            throw new Exception("something bad:" + (e));
        }
    }
}
        