public class SBSC_USE_STRINGBUFFER_CONCATENATION {
    public void bad() {
        // This is bad
        String s = "";
        for (int i = 0; i < field.length; ++i) {
            s = s + field[i];
        }
    }
    public void good() { 
        // This is better
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < field.length; ++i) {
            buf.append(field[i]);
        }
        String s = buf.toString();
    }
}