

String unitAbbrev = "μs";      // OK, a normal String
String unitAbbrev = "\u03bcs"; // violation, not all characters are escaped ('s').
String unitAbbrev = "\u03bc\u03bc\u03bc"; // OK
String unitAbbrev = "\u03bc\u03bcs";// violation, not all characters are escaped ('s').
return '\ufeff' + content;          // OK, all control characters are escaped
        