
catch (SQLException e) {
  ...
  throw new MySQLException(contextInfo, e);
}
