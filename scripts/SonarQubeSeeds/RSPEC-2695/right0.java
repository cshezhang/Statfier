
PreparedStatement ps = con.prepareStatement("SELECT fname, lname FROM employees where hireDate > ? and salary < ?");
ps.setDate(1, date);
ps.setDouble(2, salary);

ResultSet rs = ps.executeQuery();
while (rs.next()) {
  String fname = rs.getString(1);
  // ...
}
