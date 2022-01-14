
public void doSomething(File file, Lock lock) {
  if (!lock.tryLock()) {
    // lock failed; take appropriate action
  }
  if (!file.delete()) {
    // file delete failed; take appropriate action
  }
}
