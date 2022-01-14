
public class Fruit implements Serializable {
  private static final long serialVersionUID = 1;

  protected Object readResolve() throws ObjectStreamException
  {...}

  //...
}

public class Raspberry extends Fruit implements Serializable {
  //...
}
