
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class User {
  @NotNull
  private String name;
}

public class Group {
  @NotNull
  private List<User> users; // Noncompliant; User instances are not validated
}

public class MyService {
  public void login(User user) { // Noncompliant; parameter "user" is not validated
  }
}
