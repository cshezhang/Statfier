
public class MyClass {

  public enum COLOR {
    RED, GREEN, BLUE, ORANGE;
  }

  public void doSomething() {
    Set<COLOR> warm = new HashSet<COLOR>();
    warm.add(COLOR.RED);
    warm.add(COLOR.ORANGE);
  }
}
