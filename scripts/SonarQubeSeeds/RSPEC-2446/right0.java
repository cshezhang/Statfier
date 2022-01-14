
class MyThread extends Thread{

  @Override
  public void run(){
    synchronized(this){
      // ...
      notifyAll();
    }
  }
}
