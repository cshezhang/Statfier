
Pattern.compile("söme pättern", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
str.matches("(?iu)söme pättern");
str.matches("(?iu:söme) pättern");

// UNICODE_CHARACTER_CLASS implies UNICODE_CASE
Pattern.compile("söme pättern", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
str.matches("(?iU)söme pättern");
str.matches("(?iU:söme) pättern");
