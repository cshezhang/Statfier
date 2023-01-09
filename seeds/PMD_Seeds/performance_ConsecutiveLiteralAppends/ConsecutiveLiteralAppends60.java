
public class Foo {
    public void bar() {
        StringBuilder sb1 = new StringBuilder().append("abc").append("def"); // bad
        StringBuilder sb2 = new StringBuilder().append("abc"); // ok
        StringBuilder sb3 = new StringBuilder();
        sb3.append("abc").append("def"); // bad
        StringBuilder sb4 = new StringBuilder();
        sb4.append("abc"); // ok
    }
}
        