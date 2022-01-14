
public boolean authenticate(javax.servlet.http.HttpServletRequest request, DirContext ctx) throws NamingException {
  String user = request.getParameter("user");
  String pass = request.getParameter("pass");

  String filter = "(&(uid={0})(userPassword={1}))"; // Safe

  NamingEnumeration<SearchResult> results = ctx.search("ou=system", filter, new String[]{user, pass}, new SearchControls());
  return results.hasMore();
}
