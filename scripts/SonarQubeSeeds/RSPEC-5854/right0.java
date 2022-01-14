
String s = "e\u0300";
Pattern p = Pattern.compile("é|ë|è", Pattern.CANON_EQ);
System.out.println(p.matcher(s).replaceAll("e")); // print 'e'
