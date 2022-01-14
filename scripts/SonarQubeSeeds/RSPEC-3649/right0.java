
public boolean authenticate(javax.servlet.http.HttpServletRequest request, java.sql.Connection connection) throws SQLException {
  String user = request.getParameter("user");
  String pass = request.getParameter("pass");

  String query = "SELECT * FROM users WHERE user = ? AND pass = ?"; // Safe even if authenticate() method is still vulnerable to brute-force attack in this specific case

  java.sql.PreparedStatement statement = connection.prepareStatement(query);
  statement.setString(1, user); // Will be properly escaped
  statement.setString(2, pass);
  java.sql.ResultSet resultSet = statement.executeQuery();
  return resultSet.next();
}
