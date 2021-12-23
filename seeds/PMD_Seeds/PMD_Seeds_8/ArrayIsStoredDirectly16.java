
public class AISD {
    private final byte[] buf;
    private AISD(final byte[] buf) {
        this.buf = buf;
    }
    private void set(final byte[] buf) {
        this.buf = buf;
    }
    public AISD of(final byte[] buf) {
        return new AISD(buf.clone());
    }
}
        