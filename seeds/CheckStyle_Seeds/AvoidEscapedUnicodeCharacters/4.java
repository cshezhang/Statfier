

String unitAbbrev = "μs";       // OK, a normal String
String unitAbbrev1 = "\u03bcs"; // violation, printable escape character.
String unitAbbrev2 = "\u03bc\u03bc\u03bc"; // violation, printable escape character.
String unitAbbrev3 = "\u03bc\u03bcs";// violation, printable escape character.
return '\ufeff' + content;           // OK, non-printable escape character.
        