
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        int count = 0;
        sb.append("CCC" + String.valueOf(count++));
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        sb.append("CCC" + String.valueOf(count++));
    }
}
        