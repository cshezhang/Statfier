
map.computeIfAbsent(key, k -> null); // Noncompliant, the map will not contain an entry key->null.
map.computeIfPresent(key, (k, oldValue) -> null); // Noncompliant
