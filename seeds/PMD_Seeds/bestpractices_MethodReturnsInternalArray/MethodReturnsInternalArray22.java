import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class I {
  private I() {}

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Inner {
    String[] value();
  }
}

