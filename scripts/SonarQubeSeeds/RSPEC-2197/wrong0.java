
public boolean isOdd(int x) {
  return x % 2 == 1;  // Noncompliant; if x is an odd negative, x % 2 == -1
}
