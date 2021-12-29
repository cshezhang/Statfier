
public class Foo {
    void bar() {
        try {
            // do something
        }  catch (SomeException se) {
            se.printStackTrace();
            System.out.println("boo");
            int i = 1;
            se.getLocalizedMessage();
            long l = 1L + 4L;
            se.getMessage();
            int a = 1;
            se.toString();
            se.getMessage();
            System.out.println("far");
            se.getStackTrace();
        }
    }
}
        