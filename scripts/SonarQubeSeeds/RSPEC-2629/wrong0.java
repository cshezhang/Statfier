
logger.log(Level.DEBUG, "Something went wrong: " + message);  // Noncompliant; string concatenation performed even when log level too high to show DEBUG messages

logger.fine("An exception occurred with message: " + message); // Noncompliant

LOG.error("Unable to open file " + csvPath, e);  // Noncompliant

Preconditions.checkState(a > 0, "Arg must be positive, but got " + a);  // Noncompliant. String concatenation performed even when a > 0

Preconditions.checkState(condition, formatMessage());  // Noncompliant. formatMessage() invoked regardless of condition

Preconditions.checkState(condition, "message: %s", formatMessage());  // Noncompliant
