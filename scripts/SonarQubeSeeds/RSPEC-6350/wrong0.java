
String input = request.getParameter("input");
String cmd[] =  new String[] { "/usr/bin/find", input };
Runtime.getRuntime().exec(cmd); // Sensitive
