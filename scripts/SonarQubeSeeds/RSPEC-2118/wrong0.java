
public class Vegetable {  // neither implements Serializable nor extends a class that does
  //...
}

public class Menu {
  public void meal() throws IOException {
    Vegetable veg;
    //...
    FileOutputStream fout = new FileOutputStream(veg.getName());
    ObjectOutputStream oos = new ObjectOutputStream(fout);
    oos.writeObject(veg);  // Noncompliant. Nothing will be written
  }
}
