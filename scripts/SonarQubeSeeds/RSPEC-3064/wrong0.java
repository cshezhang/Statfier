
public class MyClass {

  private volatile List<String> strings;

  public List<String> getStrings() {
    if (strings == null) {  // check#1
      synchronized(MyClass.class) {
        if (strings == null) {
          strings = new ArrayList<>();  // Noncompliant
          strings.add("Hello");  //When threadA gets here, threadB can skip the synchronized block because check#1 is false
          strings.add("World");
        }
      }
    }
    return strings;
  }
}
