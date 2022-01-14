
public class Foo{
   private int field = 0;
   public boolean isTrue() {...}
   public Foo() {...}                         // Noncompliant, constructor defined after methods
   public static final int OPEN = 4;  //Noncompliant, variable defined after constructors and methods
}
