

import com.neurologic.http.impl.ApacheHttpClient; // OK
import static java.awt.Button.A;
import javax.swing.JComponent; // violation, wrong order, caused by above static import
                               // all static imports comes at bottom
import java.net.URL; // violation, extra separation in import group
import java.security.KeyManagementException;
import javax.swing.JComponent; // violation, wrong order, 'javax' should be above 'java' imports
import com.neurologic.http.HttpClient; // violation, wrong order, 'com' imports should be at top

public class TestClass { }
        