package iter0;

public class UnnecessaryLocalBeforeReturnFP {
    public java.lang.Object test2() {
        int i = 0;
        java.lang.Object o = thing()
            .make(i);
        return o; // true positive
    }
}
        