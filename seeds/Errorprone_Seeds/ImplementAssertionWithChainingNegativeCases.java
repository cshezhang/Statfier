/*
 * Copyright 2019 The Error Prone Authors.
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

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;

/** @author cpovirk@google.com (Chris Povirk) */
public class ImplementAssertionWithChainingNegativeCases {
  static final class FooSubject extends Subject {
    private final Foo actual;

    private FooSubject(FailureMetadata metadata, Foo actual) {
      super(metadata, actual);
      this.actual = actual;
    }

    void doesNotHaveString(String other) {
      if (actual.string().equals(other)) {
        failWithActual("expected not to have string", other);
      }
    }

    void doesNotHaveInteger(int other) {
      if (actual.integer() == other) {
        failWithActual("expected not to have integer", other);
      }
    }

    void hasBoxedIntegerSameInstance(Integer expected) {
      if (actual.boxedInteger() != expected) {
        failWithActual("expected to have boxed integer", expected);
      }
    }
  }

  private static final class Foo {
    final String string;
    final int integer;
    final Integer boxedInteger;

    Foo(String string, int integer, Integer boxedInteger) {
      this.string = string;
      this.integer = integer;
      this.boxedInteger = boxedInteger;
    }

    String string() {
      return string;
    }

    int integer() {
      return integer;
    }

    Integer boxedInteger() {
      return boxedInteger;
    }

    Foo otherFoo() {
      return this;
    }
  }
}
