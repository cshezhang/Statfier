
public class Address {
  //...
}

public class Person implements Serializable {
  private static final long serialVersionUID = 1905122041950251207L;

  private String name;
  private Address address;  // Noncompliant; Address isn't serializable
}
