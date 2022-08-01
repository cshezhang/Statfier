
import java.util.Properties;
public class Foo {
    public Long getData(User usr) {
        return usr.getId();
    }
    public String getData(Properties props) {
        return "Props: " + props.toString();
    }
    public void bar() {
        String s = getData(new Properties()).toString();
    }
}
        