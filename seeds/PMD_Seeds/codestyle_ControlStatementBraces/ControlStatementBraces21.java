
public class Foo {
    void foo() {
        int x = 0;
        if (true)
            x++; // here
         else if (false) {
            x--;
        } else

            ; // and here
    }
}
        