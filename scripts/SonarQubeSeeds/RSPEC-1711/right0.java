
@FunctionalInterface
public interface ExtendedBooleanSupplier extends BooleanSupplier { // Compliant, extends java.util.function.BooleanSupplier
  default boolean isFalse() {
    return !getAsBoolean();
  }
}

public class MyClass {
    private int a;
    public double myMethod(IntToDoubleFunction instance){
	return instance.applyAsDouble(a);
    }
}
