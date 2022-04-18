

String unitAbbrev = "μs";      // OK, a normal String
String unitAbbrev = "\u03bcs"; // OK, Greek letter mu, "s"
return '\ufeff' + content;
// -----^--------------------- violation, comment is not used within same line.
        