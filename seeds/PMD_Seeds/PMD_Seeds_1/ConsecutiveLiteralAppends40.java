
public class Foo {
    StringBuffer sb = new StringBuffer();
    public void foo() {
        sb.append("World");
    }
    public void bar() {
        sb.append("World");
    }
}

public class Foo2 {
    StringBuilder sb = new StringBuilder();
    public void foo() {
        sb.append("World");
    }
    public void bar() {
        sb.append("World");
    }
}
        