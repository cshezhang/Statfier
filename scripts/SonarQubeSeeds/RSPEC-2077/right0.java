
public User getUser(Connection con, String user) throws SQLException {

  Statement stmt1 = null;
  PreparedStatement pstmt = null;
  String query = "select FNAME, LNAME, SSN " +
                 "from USERS where UNAME=?"
  try {
    stmt1 = con.createStatement();
    ResultSet rs1 = stmt1.executeQuery("GETDATE()");

    pstmt = con.prepareStatement(query);
    pstmt.setString(1, user);  // Good; PreparedStatements escape their inputs.
    ResultSet rs2 = pstmt.executeQuery();

    //...
  }
}

public User getUserHibernate(org.hibernate.Session session, String data) {

  org.hibernate.Query query =  session.createQuery("FROM students where fname = ?");
  query = query.setParameter(0,data);  // Good; Parameter binding escapes all input

  org.hibernate.Query query2 =  session.createQuery("FROM students where fname = " + data); // Sensitive
  // ...
