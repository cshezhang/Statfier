

public final class Person {
  static interface Address1 {  // valid
  }

  interface Address2 {  // violation
  }

  static enum Age1 {  // valid
    CHILD, ADULT
  }

  enum Age2 {  // violation
    CHILD, ADULT
  }

  public static record GoodRecord() {} // valid
  public record BadRecord() {} // violation

  public static record OuterRecord() {
    static record InnerRecord1(){} // valid
    record InnerRecord2(){} // violation
  }
}
        