
protected void finalize() {
  releaseSomeResources();
  super.finalize();
}
