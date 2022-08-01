
public class ShortVariable {
    public void bar() {
        String thisIsOk = a -> foo();
        String foo = (a, b) -> foo();
        String bar = (String a, Boolean b) -> foo();
    }
}
        