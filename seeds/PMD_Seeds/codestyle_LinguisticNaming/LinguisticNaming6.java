
public class MethodTypeAndNameIsInconsistentWithSetters {
    int settlement() {
        return 1;
    }

    int setName() { // violation
        return 1;
    }

    void setFlag() {
        // do something
    }
}
        