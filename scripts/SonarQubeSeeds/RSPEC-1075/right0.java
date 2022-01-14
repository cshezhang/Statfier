
public class Foo {
  // Configuration is a class that returns customizable properties: it can be mocked to be injected during tests.
  private Configuration config;
  public Foo(Configuration myConfig) {
    this.config = myConfig;
  }
  public Collection<User> listUsers() {
    // Find here the way to get the correct folder, in this case using the Configuration object
    String listingFolder = config.getProperty("myApplication.listingFolder");
    // and use this parameter instead of the hard coded path
    File userList = new File(listingFolder, "users.txt"); // Compliant
    Collection<User> users = parse(userList);
    return users;
  }
}
