
public enum Fruit {
   APPLE, BANANA, GRAPE
}

public enum Cake {
  LEMON_TART, CHEESE_CAKE
}

public boolean isFruitGrape(Fruit candidateFruit) {
  return candidateFruit.equals(Fruit.GRAPE); // Noncompliant; this will raise an NPE if candidateFruit is NULL
}

public boolean isFruitGrape(Cake candidateFruit) {
  return candidateFruit.equals(Fruit.GRAPE); // Noncompliant; always returns false
}
