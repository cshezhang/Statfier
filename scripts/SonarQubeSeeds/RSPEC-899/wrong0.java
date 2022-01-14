
public void doSomething(File file, Lock lock) {
  file.delete();  // Noncompliant
  // ...
  lock.tryLock(); // Noncompliant
}
