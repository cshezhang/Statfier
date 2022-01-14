
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

public class Raspberry extends Fruit {
  private Color ripeColor;

  public boolean equals(Object obj) {
    if (! super.equals(obj)) {
      return false;
    }
    Raspberry fobj = (Raspberry) obj;
    if (ripeColor.equals(fobj.getRipeColor()) {  // added fields are tested
      return true;
    }
    return false;
  }
}
