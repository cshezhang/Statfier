
public class Fruit {
  protected Season ripe;
  protected Color flesh;

  // ...
}

public class Raspberry extends Fruit {
  private boolean ripe;  // Noncompliant
  private static Color FLESH; // Noncompliant
}
