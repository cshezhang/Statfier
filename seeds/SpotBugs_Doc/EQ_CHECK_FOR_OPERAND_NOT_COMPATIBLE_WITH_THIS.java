public class EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS {
  public boolean equals(Object o) {
    if (o instanceof Foo) return name.equals(((Foo) o).name);
    else if (o instanceof String) return name.equals(o);
    else return false;
  }
}

