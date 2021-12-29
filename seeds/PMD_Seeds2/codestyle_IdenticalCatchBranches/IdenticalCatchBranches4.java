
class Foo {
    {
        try {
            // do something
        } catch (Exception1 exception) {
            throw new Exception3("Error message 1", exception);
        } catch (Exception2 exception) {
            throw new Exception3("Error message 2", exception);
        }
    }
}
        