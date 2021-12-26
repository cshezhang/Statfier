
public class MethodTypeAndNameIsInconsistentWithPrefixHave {
    int havelock() {
        return 1;
    }

    int haveChild() { // violation
        return 1;
    }

    boolean haveHorn() {
        return true;
    }
}
        