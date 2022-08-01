
class Foo {
    static {
        try {
            // do something
        } catch (Exception1 exception) {
            exception(exception);
        } catch (Exception2 exception2) {
            exception2(exception2);
        }
    }

    private static void exception(Exception exception) {
        throw new Exception3("Error message 1", exception);
    }


    private static exception2(Exception exception) {
        throw new Exception3("Error message 2", exception);
    }
}
        