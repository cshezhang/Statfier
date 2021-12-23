
// javax.jms.* is not on the (aux)classpath during unit tests
import javax.jms.Connection;
import javax.jms.Session;

public class CloseResourceJMS {
    public void run() {
        Session session = resourceFactory.getSession(resourceHolder);
        if (session != null) {
            if (startConnection) {
                Connection con = resourceFactory.getConnection(resourceHolder);
                if (con != null) {
                    con.start();
                }
            }
            return session;
        }
        return null;
    }
}
        