
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("World");
        for (int ix = 0; ix < 2; ix++)
            sb.append("World");

        sb.append("World");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("World");
        for (int ix = 0; ix < 2; ix++)
            sb.append("World");

        sb.append("World");
    }
}
        