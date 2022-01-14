
public class MyServlet extends HttpServlet {
  private String userName;  //As this field is shared by all users, it's obvious that this piece of information should be managed differently
  ...
}
