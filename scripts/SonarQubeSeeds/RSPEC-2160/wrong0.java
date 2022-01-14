
public class Fruit {
  private Season ripe;

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (this.class != obj.class) {
      return false;
    }
    Fruit fobj = (Fruit) obj;
    if (ripe.equals(fobj.getRipe()) {
      return true;
    }
    return false;
  }
}

public class Raspberry extends Fruit {  // Noncompliant; instances will use Fruit's equals method
  private Color ripeColor;
}
