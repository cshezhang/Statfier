
            
            public class Foo {
              private void addVersion(final Entry entry, final Transaction txn)
                  throws PersistitInterruptedException, RollbackException {
                final TransactionIndex ti = _persistit.getTransactionIndex();
                while (true) { // +1
                  try {
                    synchronized (this) {
                      if (frst != null) { // +2 (nesting = 1)
                        if (frst.getVersion() > entry.getVersion()) { // +3 (nesting = 2)
                          throw new RollbackException();
                        }
                        if (txn.isActive()) { // +3 (nesting = 2)
                          for // +4 (nesting = 3)
                          (Entry e = frst; e != null; e = e.getPrevious()) {
                            final long version = e.getVersion();
                            final long depends = ti.wwDependency(version,
                                txn.getTransactionStatus(), 0);
                            if (depends == TIMED_OUT) { // +5 (nesting = 4)
                              throw new WWRetryException(version);
                            }
                            if (depends != 0 // +5 (nesting = 4)
                                && depends != ABORTED) { // +1
                              throw new RollbackException();
                            }
                          }
                        }
                      }
                      entry.setPrevious(frst);
                      frst = entry;
                      break;
                    }
                  } catch (final WWRetryException re) { // +2 (nesting = 1)
                    try {
                      final long depends = _persistit.getTransactionIndex()
                          .wwDependency(re.getVersionHandle(),txn.getTransactionStatus(),
                              SharedResource.DEFAULT_MAX_WAIT_TIME);
                      if (depends != 0 // +3 (nesting = 2)
                          && depends != ABORTED) { // +1
                        throw new RollbackException();
                      }
                    } catch (final InterruptedException ie) { // +3 (nesting = 2)
                      throw new PersistitInterruptedException(ie);
                    }
                  } catch (final InterruptedException ie) { // +2 (nesting = 1)
                    throw new PersistitInterruptedException(ie);
                  }
                }
              } // total complexity = 35
            }
            
        