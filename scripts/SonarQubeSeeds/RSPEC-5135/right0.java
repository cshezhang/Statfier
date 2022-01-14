
public class SecureObjectInputStream extends ObjectInputStream {
  // Constructor here

  @Override
  protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
    // Only deserialize instances of AllowedClass
    if (!osc.getName().equals(AllowedClass.class.getName())) {
      throw new InvalidClassException("Unauthorized deserialization", osc.getName());
    }
    return super.resolveClass(osc);
  }
}

public class RequestProcessor {
  protected void processRequest(HttpServletRequest request) {
    ServletInputStream sis = request.getInputStream();
    SecureObjectInputStream sois = new SecureObjectInputStream(sis);
    Object obj = sois.readObject();
  }
}
