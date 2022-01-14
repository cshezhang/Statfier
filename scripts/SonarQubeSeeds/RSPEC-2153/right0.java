
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

  int dIntValue = (int) d;

  examineInt(i);

  examineInteger(i);
  examineInteger(iger1);
}
