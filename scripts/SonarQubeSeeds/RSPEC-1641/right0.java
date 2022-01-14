
public class MyClass {

  public enum COLOR {
    RED, GREEN, BLUE, ORANGE;
  }

  public void doSomething() {
    Set<COLOR> warm = EnumSet.of(COLOR.RED, COLOR.ORANGE);
  }
}
