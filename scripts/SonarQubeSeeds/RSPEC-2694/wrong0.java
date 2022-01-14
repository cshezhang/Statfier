
public class Fruit {
  // ...

  public class Seed {  // Noncompliant; there's no use of the outer class reference so make it static
    int germinationDays = 0;
    public Seed(int germinationDays) {
      this.germinationDays = germinationDays;
    }
    public int getGerminationDays() {
      return germinationDays;
    }
  }
}
