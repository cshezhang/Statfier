
public void foo() {
  while (condition1) {
    if (condition2) {
      continue; // Noncompliant
    } else {
      doTheThing();
    }
  }
  return; // Noncompliant; this is a void method
}
