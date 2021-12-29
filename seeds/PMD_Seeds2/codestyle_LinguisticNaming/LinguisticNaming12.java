
public class AttributeTypeAndNameIsInconsistentWithPrefixIs {
    int isotherm;
    int isValid; // violation
    boolean isTrue;

    void myMethod() {
        int isothermLocal;
        int isValidLocal; // violation
        boolean isTrueLocal;
    }
}
        