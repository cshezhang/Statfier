
public class Foo {

    void method() {
        boolean halfway = false;

        try {
            halfway = true; // this may not fail so the catch block is unreachable
        } catch(Exception e) {
            System.out.println(halfway);
        }
    }
}


