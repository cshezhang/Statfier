
import net.sourceforge.pmd.*;
public class LiteralsFirstInComparisonCase {
    private static final String S2 = "s2";
    public static boolean isUnkown() {
        return PMDVersion.VERSION.equals(S2);
    }
}
        