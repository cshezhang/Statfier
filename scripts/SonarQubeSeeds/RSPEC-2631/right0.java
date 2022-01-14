
public boolean validate(javax.servlet.http.HttpServletRequest request) {
  String regex = request.getParameter("regex");
  String input = request.getParameter("input");

  input.matches(Pattern.quote(regex));  // Compliant : with Pattern.quote metacharacters or escape sequences will be given no special meaning
}
