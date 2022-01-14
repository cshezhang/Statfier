
String s = "e\u0300";
Pattern p = Pattern.compile("é|ë|è"); // Noncompliant
System.out.println(p.matcher(s).replaceAll("e")); // print 'è'
