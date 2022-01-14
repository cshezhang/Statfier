
public void doSomethingWithMap(Map<String,Object> map) {
  for (Map.Entry<String,Object> entry : map.entrySet()) {
    String key = entry.getKey();
    Object value = entry.getValue();
    // ...
  }
}
