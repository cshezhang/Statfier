
import java.sql.*;

public class StructureFactory {

    public void rechercherListe() {

        Connection _connexion = null;
        ResultSet _rs = null;
        PreparedStatement _st = null;
        try
        {
           //
        }

        finally
        {
            getProviderInstance().closeConnexion(_connexion);
            getProviderInstance().closeResultSet(_rs);
            getProviderInstance().closeStatement(_st);
        }
    }
}
        