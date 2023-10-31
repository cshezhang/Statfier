import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SslDisablerUsage {
    
    public void useAllHosts() {
        HttpsURLConnection.setDefaultHostnameVerifier(new AllHosts());
    }

    public void useTrustAllManager() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    public void useSecurityBypasser() {
        SecurityBypasser.destroyAllSSLSecurityForTheEntireVMForever();
    }
}

class SecurityBypasser {

    public static void destroyAllSSLSecurityForTheEntireVMForever() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new AllHosts());
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final KeyManagementException e) {
            e.printStackTrace();
        }
    }

}

class AllHosts implements HostnameVerifier {
    public boolean verify(final String hostname, final SSLSession session) {
        return true;
    }
}

class TrustAllManager implements X509TrustManager {

    public void checkClientTrusted(final X509Certificate[] cert, final String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(final X509Certificate[] cert, final String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}



