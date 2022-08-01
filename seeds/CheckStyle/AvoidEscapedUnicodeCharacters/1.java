

String unitAbbrev = "μs";      // OK, a normal String
String unitAbbrev = "\u03bcs"; // violation, "\u03bcs" is a printable character.
return '\ufeff' + content;     // OK, non-printable control character.
        