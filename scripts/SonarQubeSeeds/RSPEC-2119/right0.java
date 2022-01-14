
private Random rand = SecureRandom.getInstanceStrong();  // SecureRandom is preferred to Random

public void doSomethingCommon() {
  int rValue = this.rand.nextInt();
  //...
