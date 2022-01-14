
record Person(String name, int age) {
    public String getName() { // Noncompliant
        return name.toUpperCase(Locale.ROOT);
    }
}
