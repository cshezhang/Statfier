package iter0;

class Foo {
    {
        try {
            // do something
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException ise) {
            throw new RuntimeException(ise);
        }
    }
}
        