
public class MethodTypeAndNameIsInconsistentWithPrefixHas {
    int haskell() {
        return 1;
    }

    int hasChild() { // violation
        return 1;
    }

    boolean hasHorn() {
        return true;
    }
}
        