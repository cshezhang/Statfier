
public int pickNumber() {
  int i = 0;
  int j = 0;

  i = i++; // Noncompliant; i is still zero

  return j++; // Noncompliant; 0 returned
}
