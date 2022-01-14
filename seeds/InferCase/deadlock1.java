  public void lockAThenB() {
    synchronized(lockA) {
      synchronized(lockB) {
       // do something with both resources
      }
    }
  }

  public void lockBThenA() {
    synchronized(lockB) {
      synchronized(lockA) {
       // do something with both resources
      }
    }
  }