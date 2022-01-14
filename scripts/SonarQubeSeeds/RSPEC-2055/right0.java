
public class Fruit {
  private Season ripe;

  public Fruit () {...};  // Compliant; no-arg constructor added to ancestor
  public Fruit (Season ripe) {...}
  public void setRipe(Season ripe) {...}
  public Season getRipe() {...}
}

public class Raspberry extends Fruit
        implements Serializable {
  private static final long serialVersionUID = 1;

  private String variety;

  public Raspberry(Season ripe, String variety) {...}
  public void setVariety(String variety) {...}
  public String getVarity() {...}
}
