
public class RubberBall {

  private Color color;
  private int diameter;

  public RubberBall(Color color, int diameter) {
    // ...
  }

  public void bounce(float angle, float velocity) {
    // ...
  }

  private synchronized void writeObject(ObjectOutputStream stream) throws IOException { // Noncompliant
    // ...
  }
}
