
public class Foo {
    void bar() {
        StringBuffer sb = new StringBuffer();
        if ( sb.toString().equals("foo") ) {
            sb.append(",");
        }
        sb.append( "whatever" );
    }
    void bar2() {
        StringBuilder sb = new StringBuilder();
        if ( sb.toString().equals("foo") ) {
            sb.append(",");
        }
        sb.append( "whatever" );
    }
}
        