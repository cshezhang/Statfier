
class Foo {
    {
        try {
            // do something
        } catch (FieldNotFound e) {
            throw new IllegalStateException("field not found", e);
        } catch (FieldException | FieldConvertError e) {
            throw new IllegalArgumentException("field exception ", e);
        }
    }
}
        