package iter0;

public class Foo {
    StringBuffer sb = new StringBuffer(200);
    public void bar(List l) {
        sb.append("Hello");
        sb.append("How are you today world");
    }
}
public class Foo2 {
    StringBuilder sb = new StringBuilder(200);
    public void bar(List l) {
        sb.append("Hello");
        sb.append("How are you today world");
    }
}
        