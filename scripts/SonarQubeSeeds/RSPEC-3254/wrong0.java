
@MyAnnotation(arg = "def")  // Noncompliant
public class MyClass {
  // ...
}
public @interface MyAnnotation {
  String arg() default "def";
}
