
public User getUser(Connection con, String user) throws SQLException {

  Statement stmt1 = null;
  Statement stmt2 = null;
  PreparedStatement pstmt;
  try {
    stmt1 = con.createStatement();
    ResultSet rs1 = stmt1.executeQuery("GETDATE()"); // No issue; hardcoded query

    stmt2 = con.createStatement();
    ResultSet rs2 = stmt2.executeQuery("select FNAME, LNAME, SSN " +
                 "from USERS where UNAME=" + user);  // Sensitive

    pstmt = con.prepareStatement("select FNAME, LNAME, SSN " +
                 "from USERS where UNAME=" + user);  // Sensitive
    ResultSet rs3 = pstmt.executeQuery();

    //...
}

public User getUserHibernate(org.hibernate.Session session, String data) {

  org.hibernate.Query query = session.createQuery(
            "FROM students where fname = " + data);  // Sensitive
  // ...
}
