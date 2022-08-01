
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf("2"));
        sb.append(String.valueOf("3"));
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf("2"));
        sb.append(String.valueOf("3"));
    }
}
        