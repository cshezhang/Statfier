
package org.foo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(MyJUnit4Runner.class)
public class MyJUnit4Test {

  @BeforeClass
  public static void beforeAll() {
    System.out.println("beforeAll");
  }

  @AfterClass
  public static void afterAll() {
    System.out.println("AfterAll");
  }

  @Before
  public void beforeEach() {
    System.out.println("beforeEach");
  }

  @After
  public void afterEach() {
    System.out.println("afterEach");
  }

  @Test
  public void test1() throws Exception {
    System.out.println("test1");
  }

  public interface SomeTests { /* category marker */ }

  @Test
  @Category(SomeTests.class)
  public void test2() throws Exception {
    System.out.println("test2");
  }

  @Test
  @Ignore("Requires fix of #42")
  public void ignored() throws Exception {
    System.out.println("ignored");
  }
}
