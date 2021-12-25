
public class Foo {
    static final Log _LOG = LogFactory.getLog( Foo.class );
    void foo() {
        try {
        } catch (Exception e) {
            _LOG.error(e);
            _LOG.info(e);
        }
    }
}
        