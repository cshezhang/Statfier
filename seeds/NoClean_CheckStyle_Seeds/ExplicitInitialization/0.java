

public class Test {
  private int intField1 = 0; // violation
  private int intField2 = 1;
  private int intField3;

  private char charField1 = '\0'; // violation
  private char charField2 = 'b';
  private char charField3;

  private boolean boolField1 = false; // violation
  private boolean boolField2 = true;
  private boolean boolField3;

  private Obj objField1 = null; // violation
  private Obj objField2 = new Obj();
  private Obj objField3;

  private int arrField1[] = null; // violation
  private int arrField2[] = new int[10];
  private int arrField3[];
}
        