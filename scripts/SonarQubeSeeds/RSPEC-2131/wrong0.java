
int myInt = 4;
String myIntString = (new Integer(myInt)).toString(); // Noncompliant; creates & discards an Integer object
myIntString = Integer.valueOf(myInt).toString(); // Noncompliant
