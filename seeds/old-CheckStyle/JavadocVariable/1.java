

public class Test {
private int a; // OK

/**
 * Some description here
 */
private int b; // OK
protected int c; // OK
public int d; // violation, missing javadoc for public member
/*package*/ int e; // OK
}
        