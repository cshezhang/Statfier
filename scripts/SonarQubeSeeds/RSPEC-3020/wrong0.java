
public String [] getStringArray(List<String> strings) {
  return (String []) strings.toArray();  // Noncompliant; ClassCastException thrown
}
