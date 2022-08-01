
class Foo {
    {
        try {
            // do something
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }
}
        