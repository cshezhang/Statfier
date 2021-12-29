
public class Foo {
    final Foo otherFoo = Foo.this;

    public void doSomething() {
         final Foo anotherFoo = Foo.this;
    }

    private ActionListener returnListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSomethingWithQualifiedThis(Foo.this);
            }
        };
    }

    private class Foo3 {
        final Foo myFoo = Foo.this;
    }

    private class Foo2 {
        final Foo2 myFoo2 = Foo2.this;
    }
}
        