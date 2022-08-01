
import java.text.SimpleDateFormat;

public class Test {
    private static final DateFormat enFormat = new SimpleDateFormat("EEEE MMMM d, yyyy 'at' hh':'mma", Locale.US);
    private static final DateFormat frFormat = new SimpleDateFormat("EEEE 'le' d MMMM yyyy 'à' HH'h'mm", Locale.CANADA_FRENCH);

    protected DateFormat getDateFormat() {
        return getLang() == LangEnum.FR ? frFormat : enFormat;
    }
}
        