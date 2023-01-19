/*
ExplicitInitialization
onlyObjectReferences = (default)false


*/

package com.puppycrawl.tools.checkstyle.checks.coding.explicitinitialization;

public class InputExplicitInitialization {
  private int x = 0; // violation
  private Object bar = /* comment test */ null; // violation
  private int y = 1;
  private long y1 = 1 - 1;
  private long y3;
  private long y4 = 0L; // violation
  private boolean b1 = false; // violation
  private boolean b2 = true;
  private boolean b3;
  private String str = "";
  java.lang.String str1 = null, str3 = null; // 2 violations
  int ar1[] = null; // violation
  int ar2[] = new int[1];
  int ar3[];
  float f1 = 0f; // violation
  double d1 = 0.0; // violation

  static char ch;
  static char ch1 = 0; // violation
  static char ch2 = '\0'; // violation
  static char ch3 = '\1';

  void method() {
    int xx = 0;
    String s = null;
  }
}

interface interface1 {
  int TOKEN_first = 0x00;
  int TOKEN_second = 0x01;
  int TOKEN_third = 0x02;
}

class InputExplicitInit2 {
  private Bar<String> bar = null; // violation
  private Bar<String>[] barArray = null; // violation
}

enum InputExplicitInit3 {
  A,
  B {
    private int x = 0; // violation
    private Bar<String> bar = null; // violation
    private Bar<String>[] barArray = null; // violation
    private int y = 1;
  };
  private int x = 0; // violation
  private Bar<String> bar = null; // violation
  private Bar<String>[] barArray = null; // violation
  private int y = 1;
  private Boolean booleanAtt = false;
}

@interface annotation1 {
  int TOKEN_first = 0x00;
  int TOKEN_second = 0x01;
  int TOKEN_third = 0x02;
}

class ForEach {
  public ForEach(java.util.Collection<String> strings) {
    for (String s : strings) // this should not even be checked
    {}
  }
}

class Bar<T> {}

class Chars {
  char a;
  char b = a;
  byte c = 1;
  short d = 1;
  final long e = 0;
}

class Doubles {
  final double subZero = -0.0;
  final double nan = Double.NaN;
  private short shortVariable = 0; // violation
  private byte bite = 0; // violation
}

