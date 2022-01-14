
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class User {
  @NotNull
  private String name;
}

public class Group {
  @Valid
  @NotNull
  private List<User> users; // Compliant; User instances are validated

  @NotNull
  // preferred style as of Bean Validation 2.0
  private List<@Valid User> users2; // Compliant; User instances are validated
}

public class MyService {
  public void login(@Valid User user) { // Compliant
  }
}
