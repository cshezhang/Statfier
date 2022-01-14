
class MyClass {
  private static final Map<String, String> MY_MAP = new HashMap<String, String>() {

    // Noncompliant - HashMap should be extended only to add behavior, not for initialization
    {
      put("a", "b");
    }

  };
}
