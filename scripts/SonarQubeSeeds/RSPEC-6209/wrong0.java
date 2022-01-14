
record Record() implements Serializable {
  @Serial
  private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0]; // Noncompliant
  @Serial
  private void writeObject(ObjectOutputStream out) throws IOException { // Noncompliant
    ...
  }
}
record Record() implements Externalizable {
  @Override
  public void writeExternal(ObjectOutput out) throws IOException { // Noncompliant
    ...
  }
  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException { // Noncompliant
    ...
  }
}
