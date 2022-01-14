
public boolean isFruitGrape(Fruit candidateFruit) {
  return candidateFruit == Fruit.GRAPE; // Compliant; there is only one instance of Fruit.GRAPE - if candidateFruit is a GRAPE it will have the same reference as Fruit.GRAPE
}

public boolean isFruitGrape(Cake candidateFruit) {
  return candidateFruit == Fruit.GRAPE; // Compliant; compilation time failure
}
