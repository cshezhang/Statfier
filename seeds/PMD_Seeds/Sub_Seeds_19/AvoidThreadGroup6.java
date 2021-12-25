
import java.lang.ThreadGroup;

public class Foo {
    void bar() {
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlData = new java.sql.Date();
        java.math.BigInteger test = new java.math.BigInteger("123");
        throw new java.io.InvalidClassException();
    }
}
        