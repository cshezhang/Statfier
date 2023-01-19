package com.acme;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FooTest {

  @Test
  public void testBar() {
    assertThat(createBar("", ""), containsString(""));
  }

  @Test
  public void testFoo() {
    assertThat(createFoo(""), containsString(""));
  }

  private String createFoo(String a) {
    return a;
  }

  private String createBar(String b, String c) {
    return b + c;
  }
}

