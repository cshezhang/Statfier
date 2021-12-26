
public class MethodTypeAndNameIsInconsistentWithPrefixCan {
    int cannibal() {
        return 1;
    }

    int canFly() { // violation
        return 1;
    }

    boolean canWalk() {
        return true;
    }
}
        