
public class Parent {
    enum A {
        someEnum;
    }

    public void doSomethingUnqualified(A a) {
        doSomethingPrivateWithQualified(a);
    }

    private void doSomethingPrivateWithQualified(Parent.A a) {
        // PMD error because it doesn't equate Parent.A as the same type as A.
    }

    public void doSomethingQualified(Parent.A a) {
        doSomethingPrivateUnqualified(a);
    }

    private void doSomethingPrivateUnqualified(A a) {
        // PMD error because it doesn't equate Parent.A as the same type as A.
    }
}
        