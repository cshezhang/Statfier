
public void makeCopies(String[] source) {
  this.array = Arrays.copyOf(source, source.length);
  Collections.addAll(this.list, source);
}
