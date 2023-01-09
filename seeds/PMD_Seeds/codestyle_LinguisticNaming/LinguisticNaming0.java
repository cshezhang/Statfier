
public class MethodTypeAndNameIsInconsistentWithPrefixIs {
    int isotherm() {
        return 1;
    }

    int isValid() { // violation
        return 1;
    }

    boolean isSmall() {
        return true;
    }
}
        