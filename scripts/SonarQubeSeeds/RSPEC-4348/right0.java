
class FooSequence implements Iterable<Foo> {
  private Foo[] seq;

  public Iterator<Foo> iterator() {
    return new Iterator<Foo>() {
      private int idx = 0;

      public boolean hasNext() {
        return idx < seq.length;
      }

      public Foo next() {
        return seq[idx++];
      }
    };
  }
  // ...
}
