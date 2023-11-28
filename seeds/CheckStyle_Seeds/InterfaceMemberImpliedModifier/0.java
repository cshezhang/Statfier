public interface AddressFactory {

  public static final String UNKNOWN = "Unknown"; // valid

  String OTHER = "Other"; // violation

  public static AddressFactory instance(); // valid

  public abstract Address createAddress(String addressLine, String city); // valid

  List<Address> findAddresses(String city); // violation

  interface Address { // violation

    String getCity(); // violation
  }
}

