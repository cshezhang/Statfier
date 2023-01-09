
public class MethodTypeAndNameIsInconsistentWithPrefixAs {
    void aspect() {
        // do something
    }

    void asDataType() { // violation
        // do something
    }

    int asNumber() {
        return 1;
    }
}
        