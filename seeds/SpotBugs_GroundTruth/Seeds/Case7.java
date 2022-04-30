public class Case7 {
    
    public static final boolean tag = false;

    public void func() {
        if (tag) {
            System.exit(0); // A false violation warning reported here
        }
    }
}