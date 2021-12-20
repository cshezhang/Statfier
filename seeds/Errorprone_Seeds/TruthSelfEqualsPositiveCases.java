/*
 * Copyright 2016 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns.testdata;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Positive test cases for TruthSelfEquals check.
 *
 * @author bhagwani@google.com (Sumit Bhagwani)
 */
public class TruthSelfEqualsPositiveCases {

  public void testAssertThatEq() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: new EqualsTester().addEqualityGroup(test).testEquals()
    assertThat(test).isEqualTo(test);
  }

  public void testAssertWithMessageEq() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: new EqualsTester().addEqualityGroup(test).testEquals()
    assertWithMessage("msg").that(test).isEqualTo(test);
  }

  public void testAssertThatSame() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: new EqualsTester().addEqualityGroup(test).testEquals()
    assertThat(test).isSameInstanceAs(test);
  }

  public void testAssertWithMessageSame() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: new EqualsTester().addEqualityGroup(test).testEquals()
    assertWithMessage("msg").that(test).isSameInstanceAs(test);
  }

  public void testAssertThatNeq() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: isNotEqualTo method are the same object
    assertThat(test).isNotEqualTo(test);
  }

  public void testAssertThatNotSame() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: isNotSameInstanceAs method are the same object
    assertThat(test).isNotSameInstanceAs(test);
  }

  public void testAssertWithMessageNeq() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: isNotEqualTo method are the same object
    assertWithMessage("msg").that(test).isNotEqualTo(test);
  }

  public void testAssertWithMessageNotSame() {
    String test = Boolean.TRUE.toString();
    // BUG: Diagnostic contains: isNotSameInstanceAs method are the same object
    assertWithMessage("msg").that(test).isNotSameInstanceAs(test);
  }
}
