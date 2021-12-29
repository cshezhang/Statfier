
public class MethodTypeAndNameIsInconsistentWithGetters {
    void getaways() {
        // do something
    }

    void getName() { // violation
        // do something
    }

    int getCount() {
        return 1;
    }
}
        