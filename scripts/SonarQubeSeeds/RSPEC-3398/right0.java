
public class Outie {
  private int i=0;

  public class Innie {
    public void doTheThing() {
      increment();
    }

    private void increment() {
      Outie.this.i++;
    }
  }
}
