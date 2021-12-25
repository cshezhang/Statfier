
public class SystemCall {
    public static void main(String[] args) {
        new Runnable() {
            public void run() {
                // NEVER DO THIS IN A APP SERVER !!!
                System.exit(0);
            }
        };
    }
}
        