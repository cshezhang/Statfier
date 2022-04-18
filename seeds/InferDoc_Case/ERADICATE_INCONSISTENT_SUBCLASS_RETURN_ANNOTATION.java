import android.support.annotation.Nullable;

class A {
  String create() {
    return new String("abc");
  }
}

class B extends A {
  @Nullable
  String create() {  // Inconsistent @Nullable annotation.
      return null;
  }
}

class ERADICATE_INCONSISTENT_SUBCLASS_RETURN_ANNOTATION {

  int foo(A a) {
     String s = a.create();
     return s.length();
  }

  void main(String[] args) {
     A a = new B();
     foo(a);
  }

}