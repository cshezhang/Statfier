
public void example() {
  for (Foo obj : (List<Foo>) getFoos()) {  // Noncompliant; cast unnecessary because List<Foo> is what's returned
    //...
  }
}

public List<Foo> getFoos() {
  return this.foos;
}
