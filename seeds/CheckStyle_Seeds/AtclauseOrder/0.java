

/**
* Some javadoc. // OK
*
* @author Some javadoc. // OK
* @version Some javadoc. // OK
* @param Some javadoc. // OK
* @return Some javadoc. // OK
* @throws Some javadoc. // OK
* @exception Some javadoc. // OK
* @see Some javadoc. // OK
* @since Some javadoc. // OK
* @serial Some javadoc. // OK
* @serialField // OK
* @serialData // OK
* @deprecated Some javadoc. // OK
*/

class Valid implements Serializable
{
}

/**
* Some javadoc.
*
* @since Some javadoc. // OK
* @version Some javadoc. // Violation - wrong order
* @deprecated
* @see Some javadoc. // Violation - wrong order
* @author Some javadoc. // Violation - wrong order
*/

class Invalid implements Serializable
{
}
        