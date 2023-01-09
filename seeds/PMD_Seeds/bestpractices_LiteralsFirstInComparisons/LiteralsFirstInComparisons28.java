
import net.sourceforge.pmd.PMDVersion;
public class LiteralsFirstInComparisonCase {
    private static final String S1 = "s1";
    private static final String S2 = "s2";
    public static boolean compare() {
        return S1.equals(S2);
    }
    public static boolean isUnkown() {
        return PMDVersion.VERSION.equals(S2);
    }
}
        