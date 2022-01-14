
public class Raspberry extends Fruit
        implements Serializable {
  private static final long serialVersionUID = 1;
  private String variety;

  public Raspberry(Season ripe, String variety) { ...}
  public void setVariety(String variety) {...}
  public String getVarity() {...}
}
