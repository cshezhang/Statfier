
public void examineInt(int a) {
  //...
}

public void examineInteger(Integer a) {
  // ...
}

public void func() {
  int i = 0;
  Integer iger1 = Integer.valueOf(0);
  double d = 1.0;

  int dIntValue = new Double(d).intValue(); // Noncompliant

  examineInt(new Integer(i).intValue()); // Noncompliant; explicit box/unbox
  examineInt(Integer.valueOf(i));  // Noncompliant; boxed int will be auto-unboxed

  examineInteger(i); // Compliant; value is boxed but not then unboxed
  examineInteger(iger1.intValue()); // Noncompliant; unboxed int will be autoboxed

  Integer iger2 = new Integer(iger1); // Noncompliant; unnecessary unboxing, value can be reused
}
