
public String tempt(String name, boolean ofAge) {
  if (ofAge) {
    offerLiquor(name);
  } else {
    offerCandy(name);
  }
}

// ...
public void corrupt() {
  tempt("Joe", false); // does this mean not to temp Joe?
}
