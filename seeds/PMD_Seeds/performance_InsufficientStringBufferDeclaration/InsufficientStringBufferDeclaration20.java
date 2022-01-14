
public class Foo {
    public void bar() {
        int x = 3;
        StringBuffer sb = new StringBuffer(2);
        sb.append("Hello");
        sb = new StringBuffer(5);
        sb.append("How are you today world");
    }
    public void bar2() {
        int x = 3;
        StringBuilder sb = new StringBuilder(2);
        sb.append("Hello");
        sb = new StringBuilder(5);
        sb.append("How are you today world");
    }
}
        