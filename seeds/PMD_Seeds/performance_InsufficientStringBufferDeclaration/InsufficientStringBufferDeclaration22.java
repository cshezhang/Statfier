
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer();
        if (true) {
            sb.append("12345");
        } else if( l.size() == 5){
            sb.append("12345");
        } else {
            sb.append("12345");
        }
        if (true) {
            sb.append("12345");
        } else if( l.size() == 5){
            sb.append("12345");
        } else {
            sb.append("12345");
        }
    }
    public void bar2(List l) {
        StringBuilder sb = new StringBuilder();
        if (true) {
            sb.append("12345");
        } else if( l.size() == 5){
            sb.append("12345");
        } else {
            sb.append("12345");
        }
        if (true) {
            sb.append("12345");
        } else if( l.size() == 5){
            sb.append("12345");
        } else {
            sb.append("12345");
        }
    }
}
        