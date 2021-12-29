
class Foo {
    {
        Runnable someAction;
        someAction = () -> {
            var foo = new ArrayList<String>(5); // ok
            System.err.println(foo);
        };
    }
}
        