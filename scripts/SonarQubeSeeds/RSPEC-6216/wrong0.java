
record Person(String name, int age) {}

Person person = new Person("A", 26);
Field field = Person.class.getDeclaredField("name");
field.setAccessible(true); // secondary
field.set(person, "B"); // Noncompliant
