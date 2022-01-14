
public void doMath(int a) {
  double floor = Math.floor((double)a); // Noncompliant
  double ceiling = Math.ceil(4.2);  // Noncompliant
  double arcTan = Math.atan(0.0);  // Noncompliant
}
