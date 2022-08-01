
import java.beans.Statement;

public class Test {
    public void foo() {
        Statement stmt = new Statement(vo, "set" + prop, new Object[] { vector });

        try {
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException("Could not set property prop: " + prop + "of type:" + pd.getPropertyType(), e);
        }
    }
}
        