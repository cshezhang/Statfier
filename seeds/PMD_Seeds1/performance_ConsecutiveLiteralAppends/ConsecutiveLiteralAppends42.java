
public class Foo {
    public void foo() {
        StringBuffer sb = new StringBuffer();
        sb.append(foo("".length()+2));
        sb.append("World");
    }

    public void foo2() {
        StringBuilder sb = new StringBuilder();
        sb.append(foo("".length()+2));
        sb.append("World");
    }
}
        