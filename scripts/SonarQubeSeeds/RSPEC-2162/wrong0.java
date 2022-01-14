
public class Fruit extends Food {
  private Season ripe;

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (Fruit.class == obj.getClass()) { // Noncompliant; broken for child classes
      return ripe.equals(((Fruit)obj).getRipe());
    }
    if (obj instanceof Fruit ) {  // Noncompliant; broken for child classes
      return ripe.equals(((Fruit)obj).getRipe());
    }
    else if (obj instanceof Season) { // Noncompliant; symmetry broken for Season class
      // ...
    }
    //...
