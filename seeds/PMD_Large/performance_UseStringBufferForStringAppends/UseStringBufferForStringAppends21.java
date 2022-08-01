
public class Foo {
    public static String staticField1 = "";
    public String field2 = "";
    private String aVariable;

    void setStatic(String staticField1) {
        staticField1 = staticField1; // no violation!!
        this.staticField1 = staticField1;
    }

    void setStatic2(String staticField1) {
        staticField1 += staticField1; // no violation - only problematic within loops
        this.staticField1 += staticField1;
    }

    void setField(String field2) {
        field2 = field2; // no violation
        this.field2 = field2;
    }

    void setField2(String field2) {
        field2 += field2; // no violation
        this.field2 += field2;
    }

    public String method2(int val) {
        switch (val) {
            case 0:
                String aVariable = "";
                if (this.aVariable != null) {
                    aVariable = this.aVariable;
                }
                return aVariable;
        }
        return null;
    }
}
        