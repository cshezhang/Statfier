public class A {
  public static final String FOO = "0";
  private String bar;

  public void bla() {
    if (this.bar == null) {
      this.bar = FOO;
    }
  }
}

