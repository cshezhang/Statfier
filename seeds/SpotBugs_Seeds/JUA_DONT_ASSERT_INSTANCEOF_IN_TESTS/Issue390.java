package ghIssues;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Issue390 {

  // Should not trigger the lint.
  public void not_test() {
    Integer intVal = 1;
    assertTrue(intVal instanceof Integer);
  }

  @Test
  public void test() {
    Integer intVal = 1;
    assertTrue(intVal instanceof Integer);
  }
}

