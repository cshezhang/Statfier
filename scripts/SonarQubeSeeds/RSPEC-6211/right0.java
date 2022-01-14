
record Person(String name, int age) {
    @Override
    public String name() { // Compliant
        return name.toUpperCase(Locale.ROOT);
    }
}

record Person(String name, int age) {
    public String getNameUpperCase() { // Compliant
        return name.toUpperCase(Locale.ROOT);
    }
}
record Person(String name, int age) {
    public String getName() { // Compliant, is equivalent to 'name()'
        return name;
    }
}
record Person(String name, int age) {
    @Override
    public String name() { // Compliant
        return name.toUpperCase(Locale.ROOT);
    }
    public String getName() { // Compliant, equal to 'name()'
        return name.toUpperCase(Locale.ROOT);
    }
}
