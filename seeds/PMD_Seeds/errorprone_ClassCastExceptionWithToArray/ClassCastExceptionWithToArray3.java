
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;

public class Test {
    public void foo() {
        QueryRunner qr = getQueryRunner();
        List<?> parms = new java.util.ArrayList();
        // public <T> T QueryRunner.query(String sql, ResultSetHandler<T> rsh, Object... params)
        String result = (String) qr.query( sql, new ScalarHandler(), parms.toArray() );
    }
}
        