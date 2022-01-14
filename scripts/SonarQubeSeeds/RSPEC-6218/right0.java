
record Person(String[] names, int age) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Arrays.equals(names, person.names);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(age);
        result = 31 * result + Arrays.hashCode(names);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "names=" + Arrays.toString(names) +
                ", age=" + age +
                '}';
    }
}
