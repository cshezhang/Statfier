
public class S2175 {

  public static void main(String[] args) {
    String foo = "42";
    Map<Integer, Object> map = new HashMap<>();
    map.remove(foo); // Noncompliant; will return 'null' for sure because 'map' is handling only Integer keys

    // ...

    List<String> list = new ArrayList<String>();
    Integer integer = Integer.valueOf(1);
    if (list.contains(integer)) { // Noncompliant; always false.
      list.remove(integer); // Noncompliant; list.add(integer) doesn't compile, so this will always return 'false'
    }
  }

}
