package iter0;

public class NullAssignmentFP {
    public DateTime foo(DateTime dateTime) {
        return dateTime.getYear() < 2100 ? dateTime : null;
    }
}
        