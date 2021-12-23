
public class NestedClassPrivateMethods {
    public void doPublic() {
        doPrivate(new B());
        doPrivate2(new C());
    }

    public void doPublic(C c) {
        doPrivate3(c);
    }

    // incorrectly UnusedPrivateMethod
    private void doPrivate(I i) {}
    private void doPrivate2(I i) {}
    private void doPrivate3(I i) {}

    private interface I {
        void visit();
    }

    private class B implements I {
        public void visit() {
        }
    }

    private class C extends B {
    }
}
        