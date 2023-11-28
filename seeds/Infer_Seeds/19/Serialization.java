import codetoanalyze.java.InferTaint;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Serialization {

  // we could warn on only particular calls to the tainted ObjectInputStream (e.g., readObject,
  // readUnshared, but nothing good can come from creating a tainted ObjectInputStream
  Object taintedObjectInputStreamBad() throws IOException, ClassNotFoundException {
    Object source = InferTaint.inferSecretSource();
    ObjectInputStream stream = new ObjectInputStream((InputStream) source); // report here
    return stream.readObject();
  }
}

