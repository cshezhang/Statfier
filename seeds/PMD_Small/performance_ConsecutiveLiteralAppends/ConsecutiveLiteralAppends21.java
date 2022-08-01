
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("BBB");
        if (true) {
            sb.append("CCC");
        }
        sb.append("DDD");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("BBB");
        if (true) {
            sb.append("CCC");
        }
        sb.append("DDD");
    }
}
        