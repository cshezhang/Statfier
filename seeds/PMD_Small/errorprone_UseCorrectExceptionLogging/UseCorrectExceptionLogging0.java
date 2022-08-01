
public class Foo {
    static final Log _LOG = LogFactory.getLog( Foo.class );
    void foo() {
        try {
        } catch (OtherException oe) {
            _LOG.error(oe.getMessage(), oe);
        }
    }
}
        