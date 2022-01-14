
ResultSet rs = stmt.executeQuery("SELECT name, address FROM PERSON");
while (rs.next()) {
  // process row
}
