/*
 * Copyright 2012 The Error Prone Authors.
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

/** @author alexeagle@google.com (Alex Eagle) */
public class ReturnValueIgnoredPositiveCases {
  String a = "thing";

  // BUG: Diagnostic contains: Return value of 'valueOf' must be used
  private Runnable r = () -> String.valueOf("");

  { // String methods
    // BUG: Diagnostic contains: remove this line
    String.format("%d", 10);
    // BUG: Diagnostic contains: remove this line
    String.format("%d", 10).trim();
    // BUG: Diagnostic contains: remove this line
    java.lang.String.format("%d", 10).trim();
    // BUG: Diagnostic contains: a = a.intern()
    a.intern();
    // BUG: Diagnostic contains: a = a.trim()
    a.trim();
    // BUG: Diagnostic contains: a = a.trim().concat("b")
    a.trim().concat("b");
    // BUG: Diagnostic contains: a = a.concat("append this")
    a.concat("append this");
    // BUG: Diagnostic contains: a = a.replace('t', 'b')
    a.replace('t', 'b');
    // BUG: Diagnostic contains: a = a.replace("thi", "fli")
    a.replace("thi", "fli");
    // BUG: Diagnostic contains: a = a.replaceAll("i", "b")
    a.replaceAll("i", "b");
    // BUG: Diagnostic contains: a = a.replaceFirst("a", "b")
    a.replaceFirst("a", "b");
    // BUG: Diagnostic contains: a = a.toLowerCase()
    a.toLowerCase();
    // BUG: Diagnostic contains: a = a.toLowerCase(Locale.ENGLISH)
    a.toLowerCase(Locale.ENGLISH);
    // BUG: Diagnostic contains: a = a.toUpperCase()
    a.toUpperCase();
    // BUG: Diagnostic contains: a = a.toUpperCase(Locale.ENGLISH)
    a.toUpperCase(Locale.ENGLISH);
    // BUG: Diagnostic contains: a = a.substring(0)
    a.substring(0);
    // BUG: Diagnostic contains: a = a.substring(0, 1)
    a.substring(0, 1);
  }

  StringBuffer sb = new StringBuffer("hello");

  {
    // BUG: Diagnostic contains: remove this line
    sb.toString().trim();
  }

  BigInteger b = new BigInteger("123456789");

  { // BigInteger methods
    // BUG: Diagnostic contains: b = b.add(new BigInteger("3"))
    b.add(new BigInteger("3"));
    // BUG: Diagnostic contains: b = b.abs()
    b.abs();
    // BUG: Diagnostic contains: b = b.shiftLeft(3)
    b.shiftLeft(3);
    // BUG: Diagnostic contains: b = b.subtract(BigInteger.TEN)
    b.subtract(BigInteger.TEN);
  }

  BigDecimal c = new BigDecimal("1234.5678");

  { // BigDecimal methods
    // BUG: Diagnostic contains: c = c.add(new BigDecimal("1.3"))
    c.add(new BigDecimal("1.3"));
    // BUG: Diagnostic contains: c = c.abs()
    c.abs();
    // BUG: Diagnostic contains: c = c.divide(new BigDecimal("4.5"))
    c.divide(new BigDecimal("4.5"));
    // BUG: Diagnostic contains: remove this line
    new BigDecimal("10").add(c);
  }

  Path p = Paths.get("foo/bar/baz");

  { // Path methods
    // BUG: Diagnostic contains: p = p.getFileName();
    p.getFileName();
    // BUG: Diagnostic contains: p = p.getName(0);
    p.getName(0);
    // BUG: Diagnostic contains: p = p.getParent();
    p.getParent();
    // BUG: Diagnostic contains: p = p.getRoot();
    p.getRoot();
    // BUG: Diagnostic contains: p = p.normalize();
    p.normalize();
    // BUG: Diagnostic contains: p = p.relativize(p);
    p.relativize(p);
    // BUG: Diagnostic contains: p = p.resolve(p);
    p.resolve(p);
    // BUG: Diagnostic contains: p = p.resolve("string");
    p.resolve("string");
    // BUG: Diagnostic contains: p = p.resolveSibling(p);
    p.resolveSibling(p);
    // BUG: Diagnostic contains: p = p.resolveSibling("string");
    p.resolveSibling("string");
    // BUG: Diagnostic contains: p = p.subpath(0, 1);
    p.subpath(0, 1);
    // BUG: Diagnostic contains: p = p.toAbsolutePath();
    p.toAbsolutePath();
    try {
      // BUG: Diagnostic contains: p = p.toRealPath();
      p.toRealPath();
    } catch (IOException e) {
    }
  }

  int[] numbers = {5, 4, 3, 2, 1};
  Object[] objects = {new Object(), new Object()};

  { // Arrays methods
    // BUG: Diagnostic contains: Return value of 'asList' must be used
    Arrays.asList(5, 4, 3, 2, 1);
    // BUG: Diagnostic contains: Return value of 'binarySearch' must be used
    Arrays.binarySearch(numbers, 3);
    // BUG: Diagnostic contains: Return value of 'copyOf' must be used
    Arrays.copyOf(numbers, 3);
    // BUG: Diagnostic contains: Return value of 'copyOfRange' must be used
    Arrays.copyOfRange(numbers, 1, 3);
    // BUG: Diagnostic contains: Return value of 'deepEquals' must be used
    Arrays.deepEquals(objects, objects);
    // BUG: Diagnostic contains: Return value of 'deepHashCode' must be used
    Arrays.deepHashCode(objects);
    // BUG: Diagnostic contains: Return value of 'deepToString' must be used
    Arrays.deepToString(objects);
    // BUG: Diagnostic contains: Return value of 'equals' must be used
    Arrays.equals(objects, objects);
    // BUG: Diagnostic contains: Return value of 'hashCode' must be used
    Arrays.hashCode(objects);
    // BUG: Diagnostic contains: Return value of 'toString' must be used
    Arrays.toString(objects);
  }
}
