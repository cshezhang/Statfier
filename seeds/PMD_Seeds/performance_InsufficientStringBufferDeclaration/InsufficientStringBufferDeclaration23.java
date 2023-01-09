
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer();
        if (true) {
            sb.append("This should use");
        } else if( l.size() == 5){
            sb.append("The longest if");
        } else {
            sb.append("statement for its violation, which is this one");
        }
    }
    public void bar2(List l) {
        StringBuilder sb = new StringBuilder();
        if (true) {
            sb.append("This should use");
        } else if( l.size() == 5){
            sb.append("The longest if");
        } else {
            sb.append("statement for its violation, which is this one");
        }
    }
}
        