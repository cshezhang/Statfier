
public class Test {
    private short getPrefix(long id) {
        return 0;
    }
    private short getTimestamp(long id) {
        return 0;
    }
    private short getCounter(long id) {
        return 0;
    }
    public String toShortString(long id) {
        return String.valueOf(getPrefix(id)) + getTimestamp(id) + getCounter(id); // (12)
    }
}
        