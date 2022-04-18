

class MyClass1<T> {}        // violation
class MyClass2<t> {}        // violation
class MyClass3<abc> {}      // violation
class MyClass4<LISTENER> {} // OK
class MyClass5<RequestT> {} // violation
        