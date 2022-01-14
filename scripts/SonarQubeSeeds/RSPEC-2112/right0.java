
public void checkUrl(URL url) {
  Set<URI> sites = new HashSet<URI>();  // Compliant

  URI homepage = new URI("http://sonarsource.com");  // Compliant
  URI uri = url.toURI();
  if (homepage.equals(uri)) {  // Compliant
    // ...
  }
}
