
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(30);
        sb.append("foo").append("this is presized just right");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder(30);
        sb.append("foo").append("this is presized just right");
    }
}
        