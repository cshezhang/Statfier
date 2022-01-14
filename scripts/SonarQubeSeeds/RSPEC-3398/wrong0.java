
public class Outie {
  private int i=0;

  private void increment() {  // Noncompliant
    i++;
  }

  public class Innie {
    public void doTheThing() {
      Outie.this.increment();
    }
  }
}
