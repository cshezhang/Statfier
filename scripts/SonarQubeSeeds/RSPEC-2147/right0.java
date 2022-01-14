
catch (IOException|SQLException e) {
  doCleanup();
  logger.log(e);
}
catch (TimeoutException e) {
  doCleanup();
  throw e;
}
