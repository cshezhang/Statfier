

public class MyClass {
    public int counterXYZ = 1;                // violation
    public final int customerID = 2;          // violation
    public static int nextID = 3;             // OK, ignored
    public static final int MAX_ALLOWED = 4;  // violation
}
        