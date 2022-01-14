
public class Watermelon implements Serializable {
  // ...
  void writeObject(java.io.ObjectOutputStream out)// Noncompliant; not private
        throws IOException
  {...}

  private void readObject(java.io.ObjectInputStream in)
  {...}

  public void readObjectNoData()  // Noncompliant; not private
  {...}

  static Object readResolve() throws ObjectStreamException  // Noncompliant; this method may have any access modifier, may not be static

  Watermelon writeReplace() throws ObjectStreamException // Noncompliant; this method may have any access modifier, but must return Object
  {...}
}
