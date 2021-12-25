
public class Foo {
    private void readObject(ObjectInputStream stream) throws InvalidObjectException{
       throw new InvalidObjectException("Proxy required");
    }
}
        