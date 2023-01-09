import edu.umd.cs.findbugs.annotations.DesireWarning;

public class ArrayToString {

    private static final String[] gargs = new String[] { "1", "2" };

    public static void main(String[] args) {
        ArrayToString a = new ArrayToString();
        a.print2();
    }

    @DesireWarning("DMI_INVOKING_TOSTRING_ON_ANONYMOUS_ARRAY")
    public void print2() {
        System.out.println((new String[] { "one", "two" }).toString());
    }

}
