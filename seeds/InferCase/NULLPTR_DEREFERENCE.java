  p = foo(); // foo() might return null
  stuff();
  p.goo();   // dereferencing p, potential NPE


  import static com.google.common.base.Preconditions.checkNotNull;

  //... intervening code

  p = checkNotNull(foo()); // foo() might return null
  stuff();
  p.goo(); // p cannot be null here