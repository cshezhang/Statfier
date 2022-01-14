
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = null;
        sb = new StringBuffer(20);
        sb.append(l.toString());
    }
    public void bar2(List l) {
        StringBuilder sb = null;
        sb = new StringBuilder(20);
        sb.append(l.toString());
    }
}
        