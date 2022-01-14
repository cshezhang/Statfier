
stmt.executeQuery("SELECT name, address FROM PERSON");
ResultSet rs = stmt.getResultSet();
while (! rs.isLast()) { // Noncompliant
  // process row
}
