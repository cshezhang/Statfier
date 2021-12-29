
public class Foo {
    void bar() {
        try {
            // do something
        }  catch (SomeException se) {
            se.printStackTrace();
            System.out.println("boo");
            long l = 1L + 4L;
            se.getMessage();
            int a = 1;
            se.getMessage();
        }
    }
}
        