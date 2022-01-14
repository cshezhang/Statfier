
public class Raspberry extends Fruit  // Noncompliant; no serialVersionUID.
        implements Serializable {
  private String variety;

  public Raspberry(Season ripe, String variety) { ...}
  public void setVariety(String variety) {...}
  public String getVarity() {...}
}

public class Raspberry extends Fruit
        implements Serializable {
  private final int serialVersionUID = 1; // Noncompliant; not static & int rather than long
