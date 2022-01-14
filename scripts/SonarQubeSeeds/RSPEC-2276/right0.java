
public void doSomething(){
  synchronized(monitor) {
    while(notReady()){
      monitor.wait(200);
    }
    process();
  }
  ...
}
