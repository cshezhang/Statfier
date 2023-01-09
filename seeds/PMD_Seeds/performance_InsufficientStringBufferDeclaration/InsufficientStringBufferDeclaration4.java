
import java.util.List;
public class Foo {
    public void bar(List l) {
        int x = 3;
        StringBuffer sb = new StringBuffer(5);
        sb.append("Hello");
        sb = new StringBuffer(23);
        sb.append("How are you today world");
    }

    public void bar2(List l) {
        int x = 3;
        StringBuilder sb = new StringBuilder(5);
        sb.append("Hello");
        sb = new StringBuffer(23);
        sb.append("How are you today world");
    }
}
        