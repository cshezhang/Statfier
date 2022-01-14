
String password = System.getProperty("database.password");
Connection conn = DriverManager.getConnection("jdbc:derby:memory:myDB;create=true", "login", password);
