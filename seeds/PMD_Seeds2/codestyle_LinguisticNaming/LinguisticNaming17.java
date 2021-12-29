
public class AttributeTypeAndNameIsInconsistentWithPrefixShould {
    int shoulder;
    int shouldClimb; // violation
    boolean shouldEat;

    void myMethod() {
        int shoulderLocal;
        int shouldClimbLocal; // violation
        boolean shouldEatLocal;
    }
}
        