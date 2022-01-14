
V value = map.get(key);
if (value == null) {  // Noncompliant
  value = V.createFor(key);
  if (value != null) {
    map.put(key, value);
  }
}
if (!map.containsKey(key)) {  // Noncompliant
  value = V.createFor(key);
  if (value != null) {
    map.put(key, value);
  }
}
return value;
