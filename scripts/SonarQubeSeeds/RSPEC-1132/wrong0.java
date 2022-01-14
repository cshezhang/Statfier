
String myString = null;

System.out.println("Equal? " + myString.equals("foo"));                        // Noncompliant; will raise a NPE
System.out.println("Equal? " + (myString != null && myString.equals("foo")));  // Noncompliant; null check could be removed
