
public void doSomething(){
  synchronized(monitor) {
    while(notReady()){
      Thread.sleep(200);
    }
    process();
  }
  ...
}
