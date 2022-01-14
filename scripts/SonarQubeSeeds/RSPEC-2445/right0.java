
private String color = "red";
private final Object lockObj = new Object();

private void doSomething(){
  synchronized(lockObj) {
    //...
    color = "green";
    // ...
  }
}
