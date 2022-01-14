
public void makeCopies(String[] source) {

  this.array = new String[source.length];
  this.list = new ArrayList(source.length);

  for (int i = 0; i < source.length; i++) {
    this.array[i] = source[i]; // Noncompliant
  }

  for (String s : source) {
    this.list.add(s); // Noncompliant
  }
}
