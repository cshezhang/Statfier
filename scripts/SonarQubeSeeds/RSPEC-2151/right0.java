
public static void main(String [] args) {
  Runtime.addShutdownHook(new Runnable() {
    public void run(){
      doSomething();
    }
  });
  //...
