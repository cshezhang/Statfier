
public class ThreadLocalUserSession implements UserSession {

  private static final ThreadLocal<UserSession> DELEGATE = new ThreadLocal<>();

  public UserSession get() {
    UserSession session = DELEGATE.get();
    if (session != null) {
      return session;
    }
    throw new UnauthorizedException("User is not authenticated");
  }

  public void set(UserSession session) {
    DELEGATE.set(session);
  }

  public void unload() {
    DELEGATE.remove(); // Compliant
  }

  // ...
}
