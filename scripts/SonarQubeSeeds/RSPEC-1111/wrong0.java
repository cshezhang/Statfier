
public void dispose() throws Throwable {
  this.finalize();                       // Noncompliant
}
