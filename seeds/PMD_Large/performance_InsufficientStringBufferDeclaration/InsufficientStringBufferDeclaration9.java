
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer(0);
        sb.append(l.get(2));
        sb.append(l.toString());
    }

    public void bar2(List l) {
        StringBuilder sb = new StringBuilder(0);
        sb.append(l.get(2));
        sb.append(l.toString());
    }
}
        