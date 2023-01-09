
public class Foo {
    private void bar() {
        String result = "";
        for (int i = 0; i < 10; i++) {
            result = result + i;
            result += i;
        }
    }
}
        