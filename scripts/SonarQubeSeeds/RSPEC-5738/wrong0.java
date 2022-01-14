
/**
 * @deprecated As of release 1.3, replaced by {@link #Fee}. Will be dropped with release 1.4.
 */
@Deprecated(forRemoval=true)
public class Foo { ... }

public class Bar {
  /**
   * @deprecated  As of release 1.7, replaced by {@link #doTheThingBetter()}
   */
  @Deprecated(forRemoval=true)
  public void doTheThing() { ... }

  public void doTheThingBetter() { ... }

  /**
   * @deprecated As of release 1.14 due to poor performances.
   */
  @Deprecated(forRemoval=false)
  public void doTheOtherThing() { ... }
}

public class Qix extends Bar {
  @Override
  public void doTheThing() { ... } // Noncompliant; don't override a deprecated method marked for removal
}

public class Bar extends Foo {  // Noncompliant; Foo is deprecated and will be removed

  public void myMethod() {
    Bar bar = new Bar();  // okay; the class isn't deprecated
    bar.doTheThing();  // Noncompliant; doTheThing method is deprecated and will be removed

    bar.doTheOtherThing(); // Okay; deprecated, but not marked for removal
  }
}
