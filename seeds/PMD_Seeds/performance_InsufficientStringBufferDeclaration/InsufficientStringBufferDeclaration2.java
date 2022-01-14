
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer(l.size());
        sb.append("Hello");
        sb.append("World");
        sb.append("How are you today world");
    }

    public void bar2(List l) {
        StringBuilder sb = new StringBuilder(l.size());
        sb.append("Hello");
        sb.append("World");
        sb.append("How are you today world");
    }
}
        