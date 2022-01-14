
public class Fruit implements Serializable {
  private static final long serialVersionUID = 1;

  private Object readResolve() throws ObjectStreamException  // Noncompliant
  {...}

  //...
}

public class Raspberry extends Fruit implements Serializable {  // No access to parent's readResolve() method
  //...
}
