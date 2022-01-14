
public class Fruit extends Food {
  private Season ripe;

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() == obj.getClass()) {
      return ripe.equals(((Fruit)obj).getRipe());
    }
    return false;
}
