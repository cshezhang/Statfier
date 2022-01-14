
final Lock lock = new ReentrantLock();
final Condition notFull  = lock.newCondition();
...
notFull.wait();
