
public double divide(int divisor, int dividend) {
  return divisor/dividend;
}

public void doTheThing() {
  int divisor = 15;
  int dividend = 5;

  double result = divide(dividend, divisor);  // Noncompliant; operation succeeds, but result is unexpected
  //...
}
