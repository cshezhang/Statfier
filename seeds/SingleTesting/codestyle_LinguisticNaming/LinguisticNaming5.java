
public class MethodTypeAndNameIsInconsistentWithPrefixShould {
    int shoulder() {
        return 1;
    }

    long shouldFly() { // violation
        return 1L;
    }

    boolean shouldWalk() {
        return true;
    }
}
        