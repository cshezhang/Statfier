
catch (SQLException e) {
  ...
  LOGGER.log(Level.ERROR,  contextInfo, e);
  throw new MySQLException(contextInfo, e);
}
