
public int shift(int a) {
  int x = a >> 32; // Noncompliant
  return a << 48;  // Noncompliant
}
