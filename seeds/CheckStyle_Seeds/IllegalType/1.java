

public class Test extends TreeSet { // OK
  public <T extends java.util.HashSet> void method() { // violation

    LinkedHashMap<Integer, String> lhmap = new LinkedHashMap<Integer, String>(); // OK

    java.lang.IllegalArgumentException illegalex; // OK
    java.util.TreeSet treeset; // Ok
  }

  public <T extends java.util.HashSet> void typeParam(T t) {} // violation

  public void fullName(TreeSet a) {} // OK

}
        