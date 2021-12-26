
class Foo {
    {
        Runnable someAction = () -> {
            var foo = new ArrayList<String>(5); // ok
            System.err.println(foo);
        };
    }
}
        