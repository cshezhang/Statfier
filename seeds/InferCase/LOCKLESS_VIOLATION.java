Interface I {
    @Lockless
    public void no_lock();
}

class LOCKLESS_VIOLATION implements I {
  private synchronized do_lock() {}

  public void no_lock() { // this method should not acquire any locks
    do_lock();
  }
}