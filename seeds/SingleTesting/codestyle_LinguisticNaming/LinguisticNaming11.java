
public class MethodTypeAndNameIsInconsistentWithPrefixTo {
    void grapeAsWine() { // violation
        // do something
    }

    int hopsAsBeer() {
        return 1;
    }

    void defaultAspectConfig() { // shouldn't report a violation, since "As" is not followed by uppercase letter
    }
}
        