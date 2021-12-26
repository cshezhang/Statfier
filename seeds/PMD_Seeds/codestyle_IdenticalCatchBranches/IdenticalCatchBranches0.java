
class Foo {
    {
        try {
            // do something
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalStateException e) { // Can be collapsed into the previous block
            throw e;
        }
    }
}
        