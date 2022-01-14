
public void callTheThing() {
  //...
  doTheThing("s1", "s2");
  doTheThing(new String[12]);
  doTheOtherThing((Object[]) new String[8]);
   // ...
}

public void doTheThing (String ... args) {
  // ...
}

public void doTheOtherThing(Object ... args) {
  // ...
}
