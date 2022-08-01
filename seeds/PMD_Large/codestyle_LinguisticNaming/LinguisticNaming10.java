
public class MethodTypeAndNameIsInconsistentWithPrefixTo {
    void grapeToWine() { // violation
        // do something
    }

    int hopsToBeer() {
        return 1;
    }

    void doneTooMuch() { // shouldn't report a violation, since "To" is not followed by uppercase letter
    }
}
        