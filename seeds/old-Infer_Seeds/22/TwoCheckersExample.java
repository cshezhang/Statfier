



import codetoanalyze.java.annotation.Expensive;
import codetoanalyze.java.annotation.PerformanceCritical;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class TwoCheckersExample {

  @Expensive
  static List shouldRaiseImmutableCastError() {
    return ImmutableList.of();
  }

  @PerformanceCritical
  static List shouldRaisePerformanceCriticalError() {
    return shouldRaiseImmutableCastError();
  }
}
