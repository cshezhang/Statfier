

import static java.lang.System.out;
import static java.lang.Math; // violation, alphabetical case sensitive ASCII order, 'M' < 'S'
import java.io.IOException;

import java.net.URL; // violation, extra separation before import
import java.security.KeyManagementException;

import javax.net.ssl.TrustManager;

import javax.net.ssl.X509TrustManager; // violation, groups should not be separated internally

import org.apache.http.conn.ClientConnectionManager;

public class SomeClass { }
        