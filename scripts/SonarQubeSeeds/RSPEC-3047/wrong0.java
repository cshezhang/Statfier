
public void doSomethingToAList(List<String> strings) {
  for (String str : strings) {
    doStep1(str);
  }
  for (String str : strings) {  // Noncompliant
    doStep2(str);
  }
}
