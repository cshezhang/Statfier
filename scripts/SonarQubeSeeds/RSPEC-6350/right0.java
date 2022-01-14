
String input = request.getParameter("input");
if (allowed.contains(input)) {
  String cmd[] =  new String[] { "/usr/bin/find", input };
  Runtime.getRuntime().exec(cmd);
}
