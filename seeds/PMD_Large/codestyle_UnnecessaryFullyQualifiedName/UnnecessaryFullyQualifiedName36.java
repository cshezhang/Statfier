
public class TestArrayType {
    String[] someArray = new String[0];

    public void foo() {
        boolean b1 = someArray instanceof String[];
        boolean b2 = someArray instanceof java.lang.String[]; // unnecessary FQN
    }
}
        