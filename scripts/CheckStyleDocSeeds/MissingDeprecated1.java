

@Deprecated
public static final int MY_CONST = 13; // ok

/** This javadoc is missing deprecated tag. */
@Deprecated
public static final int COUNTER = 10; // violation

/**
 * @deprecated
 * <p></p>
 */
@Deprecated
public static final int NUM = 123456; // ok

/**
 * @deprecated
 * <p>
 */
@Deprecated
public static final int CONST = 12; // violation, tight HTML rules not obeyed
        