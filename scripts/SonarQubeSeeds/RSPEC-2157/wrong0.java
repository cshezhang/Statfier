
class Team implements Cloneable {  // Noncompliant
  private Person coach;
  private List<Person> players;
  public void addPlayer(Person p) {...}
  public Person getCoach() {...}
}
