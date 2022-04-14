

public class Test {
  private String text;

  public void test() {} // violation, method is missing javadoc
  public String getText() { return text; } // OK
  public void setText(String text) { this.text = text; } // OK
}
        