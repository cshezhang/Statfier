
public void temptAdult(String name) {
  offerLiquor(name);
}

public void temptChild(String name) {
    offerCandy(name);
}

// ...
public void corrupt() {
  age < legalAge ? temptChild("Joe") : temptAdult("Joe");
}
