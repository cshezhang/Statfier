
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JUnit5AssertJTest {

  @Test
  void check() {
    Assertions.assertThat("FOO").isNotNull();
  }
}
        