
PreparedStatement ps = con.prepareStatement("SELECT fname, lname FROM employees where hireDate > ? and salary < ?");
ps.setDate(0, date);  // Noncompliant
ps.setDouble(3, salary);  // Noncompliant

ResultSet rs = ps.executeQuery();
while (rs.next()) {
  String fname = rs.getString(0);  // Noncompliant
  // ...
}
