

public class Foo {

 public Object clone() {return null;} // violation, overrides the clone method

 public Foo clone() {return null;} // violation, overrides the clone method

 public static Object clone(Object o) {return null;} // OK

 public static Foo clone(Foo o) {return null;} // OK

}
        