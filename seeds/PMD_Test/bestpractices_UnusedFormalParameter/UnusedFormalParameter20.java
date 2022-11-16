
public class Foo {
    // throws declaration is missing - so not the serialization readObject method
    private void readObject(ObjectInputStream stream) {
       throw new InvalidObjectException("Proxy required");
    }
    // argument is not a ObjectInputStream
    private void readObject(Foo a) throws InvalidObjectException {
       throw new InvalidObjectException("Proxy required");
    }
    // two arguments - two violations
    private void readObject(ObjectInputStream stream, Object o) throws InvalidObjectException{
       throw new InvalidObjectException("Proxy required");
    }
    // it's public
    public void readObject(ObjectInputStream stream) throws InvalidObjectException{
       throw new InvalidObjectException("Proxy required");
    }
}
        