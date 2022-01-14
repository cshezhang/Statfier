
Connection conn = null;
try {
  conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" +
        "user=steve&password=blue"); // Sensitive
  String uname = "steve";
  String password = "blue";
  conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" +
        "user=" + uname + "&password=" + password); // Sensitive

  java.net.PasswordAuthentication pa = new java.net.PasswordAuthentication("userName", "1234".toCharArray());  // Sensitive
