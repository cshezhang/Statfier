/*
 * Copyright 2017 The Error Prone Authors.
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

/**
 * @author sulku@google.com (Marsela Sulku)
 * @author mariasam@google.com (Maria Sam)
 */
public class HidingFieldPositiveCases1 {

  /** base class */
  public static class ClassA {
    protected String varOne;
    public int varTwo;
    String varThree;
  }

  /** ClassB has a field with the same name as one in its parent. */
  public static class ClassB extends ClassA {
    // BUG: Diagnostic contains: superclass: ClassA
    private String varOne = "Test";
  }

  /** ClassC has a field with the same name as one in its grandparent. */
  public static class ClassC extends ClassB {
    // BUG: Diagnostic contains: superclass: ClassA
    public int varTwo;
  }

  /**
   * ClassD has multiple fields with the same name as those in its grandparent, as well as other
   * unrelated members.
   */
  public static class ClassD extends ClassB {
    // BUG: Diagnostic contains: superclass: ClassA
    protected int varThree;
    // BUG: Diagnostic contains: superclass: ClassA
    int varTwo;
    String randOne;
    String randTwo;
  }

  /** ClassE has same variable name as grandparent */
  public static class ClassE extends ClassC {
    // BUG: Diagnostic contains: superclass: ClassC
    public String varTwo;
  }

  public static class ClassF extends ClassA {
    @SuppressWarnings("HidingField") // no warning because it's suppressed
    public String varThree;
  }

  public static class ClassG extends ClassF {
    // BUG: Diagnostic contains: superclass: ClassF
    String varThree;
  }
}
