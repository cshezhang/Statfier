/*
BooleanExpressionComplexity
max = 5
tokens = BXOR,LAND,LOR


*/

package com.puppycrawl.tools.checkstyle.checks.metrics.booleanexpressioncomplexity;

public class InputBooleanExpressionComplexity2 { // ok
  private boolean _a = false; // boolean field
  private boolean _b = false;
  private boolean _c = false;
  private boolean _d = false;
  /*public method*/
  public void foo() {
    if (_a && _b || _c ^ _d) {}

    if (((_a && (_b & _c)) || (_c ^ _d))) {}

    if (_a && _b && _c) {}

    if (_a & _b) {}

    if (_a) {}
  }

  public boolean equals(Object object) {
    new NestedClass() {
      public void method() {
        new Settings(Settings.FALSE || Settings.FALSE || Settings.FALSE || _a || _b);
      }

      public void method2() {}
    };
    return (((_a && (_b & _c)) || (_c ^ _d) || (_a && _d)));
  }

  public boolean bitwise() {
    return (((_a & (_b & _c)) | (_c ^ _d) | (_a & _d)));
  }

  public void notIgnoredMethodParameters() {
    new Settings(
        Settings.FALSE && Settings.FALSE && Settings.FALSE && Settings.TRUE && Settings.TRUE);
    new Settings(
        Settings.FALSE || Settings.FALSE || Settings.FALSE || Settings.TRUE || Settings.TRUE);
  }

  public void ignoredMethodParameters() {
    new Settings(
        Settings.RESIZABLE
            | Settings.SCROLLBARS
            | Settings.LOCATION_BAR
            | Settings.MENU_BAR
            | Settings.TOOL_BAR);
    new Settings(
        Settings.RESIZABLE
            & Settings.SCROLLBARS
            & Settings.LOCATION_BAR
            & Settings.MENU_BAR
            & Settings.TOOL_BAR);
    new Settings(
        Settings.RESIZABLE
            ^ Settings.SCROLLBARS
            ^ Settings.LOCATION_BAR
            ^ Settings.MENU_BAR
            ^ Settings.TOOL_BAR);
  }

  private class Settings {
    public static final int RESIZABLE = 1;
    public static final int SCROLLBARS = 2;
    public static final int LOCATION_BAR = 3;
    public static final int MENU_BAR = 4;
    public static final int TOOL_BAR = 5;

    public static final boolean TRUE = true;
    public static final boolean FALSE = false;

    public Settings(int flag) {}

    public Settings(boolean flag) {}
  }

  abstract class NestedClass {
    public abstract void method();

    public abstract void method2();
  }
}

