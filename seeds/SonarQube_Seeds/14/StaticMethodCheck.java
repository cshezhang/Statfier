package checks;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

class Utilities {
  private static String magicWord = "magic";
  private static String string = magicWord; // coverage
  private String otherWord = "other";

  public Utilities() {}

  private void register(final Class<?> clazz, final Object converter) {
    otherWord = "";
  }

  private String
      getMagicWord() { // Noncompliant [[sc=18;ec=30]] {{Make "getMagicWord" a "static" method.}}
    return magicWord;
  }

  private static String getMagicWordOK() {
    return magicWord;
  }

  public final String
      magicWord() { // Noncompliant [[sc=23;ec=32]] {{Make "magicWord" a "static" method.}}
    return magicWord;
  }

  public static final String magicWordOK() {
    return magicWord;
  }

  public final String getOtherWordFinal() {
    return this.otherWord;
  }

  private void setMagicWord(
      String value) { // Noncompliant {{Make "setMagicWord" a "static" method.}}
    magicWord = value;
  }

  private static void setMagicWordOK(String value) {
    magicWord = value;
  }

  private String getClassName() {
    return getClass().getSimpleName();
  }

  private void checkClassLoader() throws IllegalArgumentException {
    if (getClass().getClassLoader() != null) {
      throw new IllegalArgumentException("invalid address type");
    }
  }

  private String getOtherWord() {
    return otherWord;
  }

  private int getOtherWordLength() {
    return otherWord.length();
  }

  private String getOtherWord2() {
    return this.otherWord;
  }

  private String getOtherWord3() {
    return super.toString();
  }

  private void setOtherWord(String value) {
    otherWord = value;
    // coverage
    otherWord = value;
  }

  private int useOnlyArguments(int a, int b) { // Noncompliant
    return a + b;
  }

  private String methodOnlyOnArgument(Object obj) { // Noncompliant
    return (obj == null ? null : obj.toString());
  }

  private String attributeOnArgument(Utilities obj) { // Noncompliant
    return obj.otherWord;
  }

  class Inner {
    public Inner(String a, String b) {}

    public final String getMagicWord() {
      return "a";
    }

    public String getOuterOtherWord() {
      return Utilities.this.getOtherWord();
    }
  }

  static class Nested {
    private String getAWord() { // Noncompliant {{Make "getAWord" a "static" method.}}
      return "a";
    }
  }

  public void publicMethod() {}

  public int localAccessViasClass() { // Compliant
    return Integer.valueOf(otherWord);
  }

  private Utilities.Inner
      createInner() { // Compliant because there is a reference to an inner, non-static class
    return new Utilities.Inner("", "");
  }

  private Utilities.Nested createNested() { // Noncompliant
    return new Utilities.Nested();
  }

  private Map newMap() { // Noncompliant
    return new HashMap();
  }

  private static final int BOOLEAN_TYPE = 1;

  private void writeAnyClass(final Class<?> clazz) { // Noncompliant
    int primitiveType = 0;
    if (Boolean.TYPE.equals(clazz)) {
      primitiveType = BOOLEAN_TYPE;
    }
  }

  private <T> int sizeOfMap(Map<T, ?> map) { // Noncompliant
    return map.size();
  }

  private void callMethodOfStaticClass() { // Noncompliant
    new checks.Inner.FooBar().myHash();
  }
}

class UtilitiesExtension extends Utilities {
  public UtilitiesExtension() {}

  private void method() { // Compliant
    publicMethod();
  }
}

class SerializableExclusions implements Serializable {
  private void writeObject(java.io.ObjectOutputStream out) throws IOException {}

  private void readObject(java.io.ObjectInputStream in)
      throws IOException, ClassNotFoundException {}

  private void readObjectNoData() throws ObjectStreamException {}

  private void other() {} // Compliant, empty method

  private void recursive() { // Noncompliant
    recursive();
  }

  private void delegateOther() { // Compliant since other() is not static (although it should...)
    other();
  }

  private void readResolve() throws ObjectStreamException {
    System.out.println("foo");
  }
}

class Inner {
  static class FooBar {
    enum MyEnum {
      FOO;
    }

    private void plop() { // Noncompliant enum is static and enum constants are static
      Object o = MyEnum.FOO;
    }

    int myHash() {
      return hashCode();
    }
  }

  static class FooBarQix {
    private int instanceVariable;

    public void instanceMethod() {}

    private void
        foo() { // Compliant: foo cannot be static because it references a non-static method
      new Plopp() {
        void plop1() {
          instanceMethod();
        }
      };
    }

    private void
        init() { // Compliant: foo cannot be static because it references a non-static field
      new Plopp() {
        void plop1() {
          instanceVariable = 0;
        }
      };
    }
  }
}

class Plopp {
  Plopp() {}

  void plop1() {}
}

class SuperClass {
  public int bar;
}

class EnclosingInstance extends SuperClass {

  interface I {
    boolean gul();
  }

  private int foo;

  private void foo1() { // Compliant: use of 'EnclosingInstance.this'
    new I() {
      @Override
      public boolean gul() {
        return EnclosingInstance.this.foo == 0;
      }
    };
  }

  private void foo2() { // Compliant: use of 'EnclosingInstance.super'
    new I() {
      @Override
      public boolean gul() {
        return EnclosingInstance.super.bar == 0;
      }
    };
  }

  private void foo3() { // Compliant: use of 'EnclosingInstance.this' with fully qualified name
    new I() {
      @Override
      public boolean gul() {
        return EnclosingInstance.this.foo == 0;
      }
    };
  }

  private void foo4() { // Compliant: use of 'EnclosingInstance.super' with fully qualified name
    new I() {
      @Override
      public boolean gul() {
        return EnclosingInstance.super.bar == 0;
      }
    };
  }
}

class ParentClass {
  int value;

  public int getMagicNumber() {
    return value;
  }
}

final class ChildClass extends ParentClass {
  @Override
  public int getMagicNumber() { // OK, overrides parent method
    return 42;
  }
}

final class FinalClass {
  static int magicNumber = 42;

  public int
      getMagicNumber() { // Noncompliant [[sc=14;ec=28;quickfixes=qf_public_in_final]] {{Make
                         // "getMagicNumber" a "static" method.}}
    // fix@qf_public_in_final {{Make static}}
    // edit@qf_public_in_final [[sc=10;ec=10]]{{static }}
    return magicNumber;
  }

  int getMagicNumber2() { // Noncompliant [[sc=7;ec=22;quickfixes=qf_public_in_final2]]
    // fix@qf_public_in_final2 {{Make static}}
    // edit@qf_public_in_final2 [[sc=3;ec=3]]{{static }}
    return magicNumber;
  }

  synchronized int
      getMagicNumber3() { // Noncompliant [[sc=20;ec=35;quickfixes=qf_public_in_final3]]
    // fix@qf_public_in_final3 {{Make static}}
    // edit@qf_public_in_final3 [[sc=3;ec=3]]{{static }}
    return magicNumber;
  }

  public static int getMagicNumberOK() {
    return magicNumber;
  }
}

enum SomeEnum {
  A,
  B,
  C;

  final SomeEnum getOne() {
    return A;
  }
}

class StaticMethodCheckQuickFix {
  private static String magicWord = "magic";

  private String getMagicWord() { // Noncompliant [[sc=18;ec=30;quickfixes=qf_private]]
    // fix@qf_private {{Make static}}
    // edit@qf_private [[sc=11;ec=11]]{{static }}
    return magicWord;
  }

  @Nullable
  private String getMagicWord2() { // Noncompliant [[sc=18;ec=31;quickfixes=qf_private2]]
    // fix@qf_private2 {{Make static}}
    // edit@qf_private2 [[sc=11;ec=11]]{{static }}
    return magicWord;
  }

  public final String magicWord() { // Noncompliant [[sc=23;ec=32;quickfixes=qf_public_final]]
    // fix@qf_public_final {{Make static}}
    // edit@qf_public_final [[sc=10;ec=10]]{{static }}
    return magicWord;
  }

  private synchronized String
      magicWordSynchronized() { // Noncompliant [[sc=31;ec=52;quickfixes=qf_private_synchronized]]
    // fix@qf_private_synchronized {{Make static}}
    // edit@qf_private_synchronized [[sc=11;ec=11]]{{static }}
    return magicWord;
  }

  // Order is not following the convention: add it before the first modifier that should come after
  // static
  private synchronized String
      magicWordSynchronized2() { // Noncompliant [[sc=31;ec=53;quickfixes=qf_private_synchronized2]]
    // fix@qf_private_synchronized2 {{Make static}}
    // edit@qf_private_synchronized2 [[sc=3;ec=3]]{{static }}
    return magicWord;
  }

  private @Nullable synchronized String
      magicWordSynchronized3() { // Noncompliant [[sc=41;ec=63;quickfixes=qf_private_synchronized3]]
    // fix@qf_private_synchronized3 {{Make static}}
    // edit@qf_private_synchronized3 [[sc=21;ec=21]]{{static }}
    return magicWord;
  }
}

