
public class EnumTest {
    enum Type {
        A, B;
    }
    private final Type type = Type.A;
    public String isTypeA(Type param) {
        return param == type ? "Yes" : "No";
    }
}
        