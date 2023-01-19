import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RFE3062724 {

  abstract static class A implements Serializable {}

  static class B extends A {
    @ExpectWarning(value = "SE_BAD_FIELD", confidence = Confidence.HIGH)
    RFE3062724 notSerializable;
  }

  static class C extends A {
    @ExpectWarning(value = "SE_BAD_FIELD", confidence = Confidence.HIGH)
    RFE3062724 notSerializable;
  }

  public void writeB(ObjectOutputStream o, B b) throws IOException {
    o.writeObject(b);
  }

  public C readC(ObjectInputStream i) throws IOException, ClassNotFoundException {
    return (C) i.readObject();
  }
}

