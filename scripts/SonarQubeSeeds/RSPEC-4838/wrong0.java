
public Collection<Person> getPersons() { ... }

for (Object item : getPersons()) { // Noncompliant
  Person person = (Person) item; // Noncompliant; it's required to down-cast to the to correct type to use "item"
  person.getAdress();
}
