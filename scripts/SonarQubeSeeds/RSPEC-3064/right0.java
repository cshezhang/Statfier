
public class MyClass {

  private volatile List<String> strings;

  public List<String> getStrings() {
    if (strings == null) {  // check#1
      synchronized(MyClass.class) {
        if (strings == null) {
          List<String> tmpList = new ArrayList<>();
          tmpList.add("Hello");
          tmpList.add("World");
          strings = tmpList;
        }
      }
    }
    return strings;
  }
}
