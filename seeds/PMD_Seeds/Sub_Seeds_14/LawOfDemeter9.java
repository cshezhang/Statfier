
public class B {
    public static A a = new A();
}

public class A {
    public static void doStatic() {
    }
}

public class Foo {
    public void example() {
        A.doStatic(); // direct static - allowed
        B.a.doStatic(); // static chain - violation
    }
}
        