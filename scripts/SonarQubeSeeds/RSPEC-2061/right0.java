
public class Watermelon implements Serializable {
  // ...
  private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
  {...}

  private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
  {...}

  private void readObjectNoData()
        throws ObjectStreamException
  {...}

  protected Object readResolve() throws ObjectStreamException
  {...}

  private Object writeReplace() throws ObjectStreamException
  {...}
