
@FunctionalInterface
public interface MyInterface { // Noncompliant
	double toDouble(int a);
}

@FunctionalInterface
public interface ExtendedBooleanSupplier { // Noncompliant
  boolean get();
  default boolean isFalse() {
    return !get();
  }
}

public class MyClass {
    private int a;
    public double myMethod(MyInterface instance){
	return instance.toDouble(a);
    }
}
