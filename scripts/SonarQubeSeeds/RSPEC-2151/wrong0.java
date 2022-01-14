
public static void main(String [] args) {
  ...
  System.runFinalizersOnExit(true);  // Noncompliant
  ...
}

protected void finalize(){
  doSomething();
}
