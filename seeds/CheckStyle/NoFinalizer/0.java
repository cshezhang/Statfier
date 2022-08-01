

public class Test {

    protected void finalize() throws Throwable { // violation
        try {
           System.out.println("overriding finalize()");
        } catch (Throwable t) {
           throw t;
        } finally {
           super.finalize();
        }
    }
}
        