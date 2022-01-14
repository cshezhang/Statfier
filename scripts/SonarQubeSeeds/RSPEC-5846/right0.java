
static final Pattern p = Pattern.compile("^$", Pattern.MULTILINE);

boolean containsEmptyLines(String str) {
    return p.matcher(str).find() || str.isEmpty();
}

// ...
System.out.println(containsEmptyLines("a\n\nb")); // correctly prints 'true'
System.out.println(containsEmptyLines("")); // also correctly prints 'true'
