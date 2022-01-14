
protected void finalize() {   // Noncompliant; no call to super.finalize();
  releaseSomeResources();
}

protected void finalize() {
  super.finalize();  // Noncompliant; this call should come last
  releaseSomeResources();
}
