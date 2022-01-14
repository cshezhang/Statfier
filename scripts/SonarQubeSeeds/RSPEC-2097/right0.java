
public boolean equals(Object obj) {
  if (obj == null)
    return false;

  if (this.getClass() != obj.getClass())
    return false;

  MyClass mc = (MyClass)obj;
  // ...
}
