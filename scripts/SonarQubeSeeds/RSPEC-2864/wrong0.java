
public void doSomethingWithMap(Map<String,Object> map) {
  for (String key : map.keySet()) {  // Noncompliant; for each key the value is retrieved
    Object value = map.get(key);
    // ...
  }
}
