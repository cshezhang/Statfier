
class Team implements Cloneable {
  private Person coach;
  private List<Person> players;
  public void addPlayer(Person p) { ... }
  public Person getCoach() { ... }

  @Override
  public Object clone() {
    Team clone = (Team) super.clone();
    //...
  }
}
