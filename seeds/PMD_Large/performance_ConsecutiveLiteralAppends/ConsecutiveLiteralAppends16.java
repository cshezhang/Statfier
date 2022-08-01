
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        String foo = "World";
        sb.append("Hello" + foo);
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        String foo = "World";
        sb.append("Hello" + foo);
    }
}
        