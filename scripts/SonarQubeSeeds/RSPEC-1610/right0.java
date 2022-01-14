
public interface Car {
  public void start(Environment c);

  public default void stop(Environment c) {
    c.freeze(this);
  }
}
