
public void checkUrl(URL url) {
  Set<URL> sites = new HashSet<URL>();  // Noncompliant

  URL homepage = new URL("http://sonarsource.com");  // Compliant
  if (homepage.equals(url)) { // Noncompliant
    // ...
  }
}
