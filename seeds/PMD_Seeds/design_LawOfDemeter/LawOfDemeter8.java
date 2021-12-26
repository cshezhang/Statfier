
public class Foo {
    /**
     * This example will result in two violation.
     */
    public void example(Bar b) {
        // this method call is ok, as b is a parameter of "example"
        C c = b.getC();

        // this method call is a violation, as we are using c, which we got from B.
        // We should ask b directly instead, e.g. "b.doItOnC();"
        c.doIt();

        // this is also a violation, just differently expressed as a method chain without temporary variables.
        b.getC().doIt();

        // that's a constructor call, not a method call.
        D d = new D();
        // this method call is ok, because we have create the new instance of D locally.
        d.doSomethingElse();
    }
}
        