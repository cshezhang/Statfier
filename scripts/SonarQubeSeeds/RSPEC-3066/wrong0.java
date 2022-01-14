
public enum Continent {

  NORTH_AMERICA (23, 24709000),
  // ...
  EUROPE (50, 39310000);

  public int countryCount;  // Noncompliant
  private int landMass;

  Continent(int countryCount, int landMass) {
    // ...
  }

  public void setLandMass(int landMass) {  // Noncompliant
    this.landMass = landMass;
  }
