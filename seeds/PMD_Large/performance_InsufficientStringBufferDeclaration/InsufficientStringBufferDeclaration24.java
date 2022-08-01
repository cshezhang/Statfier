
import java.util.List;
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer();
        if (true) {
           if (true) {
                sb.append("More");
           } else if( l.size() == 5){
                sb.append("Compound");
           } else {
               sb.append("If");
           }
       } else {
           sb.append("A compound if");
       }
    }
    public void bar2(List l) {
        StringBuilder sb = new StringBuilder();
        if (true) {
           if (true) {
                sb.append("More");
           } else if( l.size() == 5){
                sb.append("Compound");
           } else {
               sb.append("If");
           }
       } else {
           sb.append("A compound if");
       }
    }
}
        