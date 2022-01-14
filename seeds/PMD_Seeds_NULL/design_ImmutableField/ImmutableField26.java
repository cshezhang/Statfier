
class Foo {
    private Bar bar;

    public Foo() {
        someMethod(() -> bar = new Bar());
    }

    private void someMethod(Runnable action) {
        action.run();
    }

    static class Bar {
    }
}
        