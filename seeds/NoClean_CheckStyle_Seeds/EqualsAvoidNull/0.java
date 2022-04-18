

String nullString = null;
nullString.equals("My_Sweet_String");            // violation
"My_Sweet_String".equals(nullString);            // OK
nullString.equalsIgnoreCase("My_Sweet_String");  // violation
"My_Sweet_String".equalsIgnoreCase(nullString);  // OK
        