

import static java.io.File.createTempFile;
import static java.lang.Math.abs; // OK, alphabetical case sensitive ASCII order, 'i' < 'l'
import java.lang.Math.sqrt; // OK, follows property 'Option' value 'above'
import java.io.File; // violation, alphabetical case sensitive ASCII order, 'i' < 'l'

import java.io.IOException; // violation, extra separation in 'java' import group

import org.albedo.*;

import static javax.WindowConstants.*; // violation, wrong order, 'javax' comes before 'org'
import javax.swing.JComponent;
import org.apache.http.ClientConnectionManager; // violation, must separate from previous import
import org.linux.apache.server.SoapServer; // OK

import com.neurologic.http.HttpClient; // OK
import com.neurologic.http.impl.ApacheHttpClient; // OK

public class SomeClass { }
        