
import java.sql.*;

public class StructureFactory {

    public void searchList() {

        Connection _connexion = pool.getConn();
        ResultSet _rs = createResultSet(_connexion);
        PreparedStatement _st = createPrepStmt(_connexion);
        Structure _structure = null;
        try
        {
           //
        }

        finally
        {
            getProviderInstance().closeConnexion(_connexion);
            getProviderInstance().closeYourEyes(_rs); //not the right method
            getProviderInstance().closeStatement(_badOne); // not the right variable
        }
    }
}
        