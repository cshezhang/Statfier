
public class Foo {

    void method() {
        boolean halfway = false;

        try {
            trySomethingWhichFails(); // catch may be reached if this throws (initializer would be used)
            halfway = true;
        } catch(Exception e) {
            System.out.println(halfway);
        }
    }
}


