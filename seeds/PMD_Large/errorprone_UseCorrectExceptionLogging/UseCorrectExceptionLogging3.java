
public class Foo {
    static class Inner {
        Log _LOG = LogFactory.getLog( Foo.class );
    }

    void foo() {
        try {
        } catch (Exception e) {
            Log _LOG = LogFactory.getLog( Main.class );
            _LOG.error(e);
            _LOG.info(e);
        }
    }
}
        