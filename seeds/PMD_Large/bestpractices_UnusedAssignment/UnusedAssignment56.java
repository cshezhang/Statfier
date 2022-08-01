
public class Foo {

    void method() {
        boolean halfway = false;

        try {
            halfway = true;
            trySomethingWhichFails(); // catch may be reached if this throws
        } catch(Exception e) {
            System.out.println(halfway);
        }
    }
}


