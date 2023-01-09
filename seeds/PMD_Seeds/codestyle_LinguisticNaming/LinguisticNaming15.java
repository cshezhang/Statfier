
public class AttributeTypeAndNameIsInconsistentWithPrefixWill {
    int william;
    int willMove; // violation
    boolean willRun;

    void myMethod() {
        int williamLocal;
        int willMoveLocal; // violation
        boolean willRunLocal;
    }
}
        