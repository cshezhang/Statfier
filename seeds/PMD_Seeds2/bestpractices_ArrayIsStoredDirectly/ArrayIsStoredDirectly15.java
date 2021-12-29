
public class AISD {
    private final byte[] buf;
    private AISD(final byte[] buf) {
        this.buf = buf;  // this is not a violation!!
    }
    private void set(final byte[] buf) {
        this.buf = buf;  // this is not a violation!!
    }
    public AISD of(final byte[] buf) {
        return new AISD(buf.clone());
    }
}
        