
record Record() implements Serializable {}

record Record() implements Serializable {
  private Object writeReplace() throws ObjectStreamException {
    ...
  }
  private Object readResolve() throws ObjectStreamException {
    ...
  }
}
