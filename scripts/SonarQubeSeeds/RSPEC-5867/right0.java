
Pattern.compile("\\p{IsAlphabetic}"); // matches all letters from all languages
Pattern.compile("\\p{IsLatin}"); // matches latin letters, including umlauts and other non-ASCII variations
Pattern.compile("\\p{Alpha}", Pattern.UNICODE_CHARACTER_CLASS);
Pattern.compile("(?U)\\p{Alpha}");
