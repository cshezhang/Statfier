
logger.log(Level.SEVERE, "Something went wrong: {0} ", message);  // String formatting only applied if needed

logger.fine("An exception occurred with message: {}", message);  // SLF4J, Log4j

logger.log(Level.SEVERE, () -> "Something went wrong: " + message); // since Java 8, we can use Supplier , which will be evaluated lazily

LOG.error("Unable to open file {0}", csvPath, e);

if (LOG.isDebugEnabled() {
  LOG.debug("Unable to open file " + csvPath, e);  // this is compliant, because it will not evaluate if log level is above debug.
}

Preconditions.checkState(arg > 0, "Arg must be positive, but got %d", a);  // String formatting only applied if needed

if (!condition) {
  throw new IllegalStateException(formatMessage());  // formatMessage() only invoked conditionally
}

if (!condition) {
  throw new IllegalStateException("message: " + formatMessage());
}
