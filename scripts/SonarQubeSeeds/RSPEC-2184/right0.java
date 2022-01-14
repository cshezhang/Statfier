
float twoThirds = 2f/3; // 2 promoted to float. Yields 0.6666667
long millisInYear = 1_000L*3_600*24*365; // 1000 promoted to long. Yields 31_536_000_000
long bigNum = Integer.MAX_VALUE + 2L; // 2 promoted to long. Yields 2_147_483_649
long bigNegNum =  Integer.MIN_VALUE-1L; // Yields -2_147_483_649
Date myDate = new Date(seconds * 1_000L);
...
public long compute(int factor){
  return factor * 10_000L;
}

public float compute2(long factor){
  return factor / 123f;
}
