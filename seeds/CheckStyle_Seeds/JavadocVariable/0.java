

public class Test {
private int a; // violation, missing javadoc for private member

/**
 * Some description here
 */
private int b; // OK
protected int c; // violation, missing javadoc for protected member
public int d; // violation, missing javadoc for public member
/*package*/ int e; // violation, missing javadoc for package member
}
        