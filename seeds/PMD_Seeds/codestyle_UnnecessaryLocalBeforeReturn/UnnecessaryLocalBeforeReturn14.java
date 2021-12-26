
public class UnnecessaryLocalBeforeReturnFP {
    public Object test2() {
        int i = 0;
        Object o = thing()
            .make(i);
        return o; // true positive
    }
}
        