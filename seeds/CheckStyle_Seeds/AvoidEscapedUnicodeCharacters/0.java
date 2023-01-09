

String unitAbbrev = "μs";     // OK, perfectly clear even without a comment.
String unitAbbrev = "\u03bcs";// violation, the reader has no idea what this is.
return '\ufeff' + content;    // OK, an example of non-printable,
                              // control characters (byte order mark).
        