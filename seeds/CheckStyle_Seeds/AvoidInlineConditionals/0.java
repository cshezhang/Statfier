

int x = 5;
boolean foobar = (x == 5); // OK

String text;
text = (text == null) ? "" : text; // violation

String b;
if (a != null && a.length() >= 1) { // OK
    b = a.substring(1);
} else {
    b = null;
}

b = (a != null && a.length() >= 1) ? a.substring(1) : null; // violation
        