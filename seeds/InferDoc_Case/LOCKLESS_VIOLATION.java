interface I_lockless {
  @Lockless
  public void no_lock();
}

class LOCKLESS_VIOLATION implements I_lockless {

  private synchronized void do_lock() {}

  public void no_lock() { // this method should not acquire any locks
    do_lock();
  }
}

