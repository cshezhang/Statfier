
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FalsePositive {
    public String foo(final String x, final String y, final double z, final Date d, final double v) {
        final StringBuilder sb = new StringBuilder(20 + 9*2 + 16 + 2 * x.length() + y.length());
        final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.GERMAN);
        df.applyPattern("#,##0.000");
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        sb.append("foobar")
            .append(df.format(z))
            .append(' ')
            .append(x)
            .append(y)
            .append("foobar")
            .append(sdf.format(d))
            .append("foobar")
            .append(df.format(v))
            .append(' ')
            .append(x);
        return sb.toString();
    }
}
        