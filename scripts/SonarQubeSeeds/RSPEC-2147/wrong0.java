
catch (IOException e) {
  doCleanup();
  logger.log(e);
}
catch (SQLException e) {  // Noncompliant
  doCleanup();
  logger.log(e);
}
catch (TimeoutException e) {  // Compliant; block contents are different
  doCleanup();
  throw e;
}
