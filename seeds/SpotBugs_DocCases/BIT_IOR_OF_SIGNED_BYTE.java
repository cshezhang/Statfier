public class BIT_IOR_OF_SIGNED_BYTE {

    public void bug() {
        int result = 0;
        for(int i = 0; i < 4; i++) {
            result = ((result << 8) | b[i]);
        }
    }

    public void nobug() {
        int result = 0;
        for(int i = 0; i < 4; i++) {
            result = ((result << 8) | (b[i] & 0xff));
        }
    }
}