
public class Foo {
    static final Log _LOG = LogFactory.getLog( Foo.class );

    static {}

    void foo() {
        try {
        } catch (Exception e) {
            _LOG.info(e);
        }
    }
}
        