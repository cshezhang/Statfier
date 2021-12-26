
public class AttributeTypeAndNameIsInconsistentWithPrefixCan {
    int cannibal;
    int canFly; // violation
    boolean canWalk;

    void myMethod() {
        int cannibalLocal;
        int canFlyLocal; // violation
        boolean canWalkLocal;
    }
}
        