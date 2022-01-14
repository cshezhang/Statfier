
public void doSomethingCommon() {
  Random rand = new Random();  // Noncompliant; new instance created with each invocation
  int rValue = rand.nextInt();
  //...
