
import java.util.List;
public class Foo {
    public void bar(List l) {
        int x = 3;
        StringBuffer sb = new StringBuffer(x);
        sb.append("Hello");
        sb.append("World");
        sb.append("How are you today world");
    }

    public void bar2(List l) {
        int x = 3;
        StringBuilder sb = new StringBuilder(x);
        sb.append("Hello");
        sb.append("World");
        sb.append("How are you today world");
    }
}
        