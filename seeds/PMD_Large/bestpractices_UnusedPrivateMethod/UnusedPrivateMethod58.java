
class Foo {
    public void bar() {
        test();
    }

    private void test(@AnnotatedUsage Foo this) {
    }
}
        