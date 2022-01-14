
URL url = new URL("https://example.org/");
HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
// Compliant; Use the default HostnameVerifier
InputStream in = urlConnection.getInputStream();
