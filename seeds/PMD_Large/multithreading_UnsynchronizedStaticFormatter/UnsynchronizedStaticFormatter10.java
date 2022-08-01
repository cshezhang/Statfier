
import org.apache.commons.lang3.time.FastDateFormat;

public class Foo {
    private static final FastDateFormat FDF = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss");
    public void format() {
        FDF.format(new Date());
    }
}
        