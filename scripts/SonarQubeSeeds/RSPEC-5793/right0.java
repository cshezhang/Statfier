
package org.foo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MyJUnit5Extension.class)
class MyJUnit5Test {

  @BeforeAll
  static void beforeAll() {
    System.out.println("beforeAll");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("afterAll");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("beforeEach");
  }

  @AfterEach
  void afterEach() {
    System.out.println("afterEach");
  }

  @Test
  void test1() {
    System.out.println("test1");
  }

  @Test
  @Tag("SomeTests")
  void test2() {
    System.out.println("test2");
  }

  @Test
  @Disabled("Requires fix of #42")
  void disabled() {
    System.out.println("ignored");
  }
}
