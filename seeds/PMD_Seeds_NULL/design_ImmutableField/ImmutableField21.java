
public class MyClass {
    private long counter = 0;

    public <E> void run(final E entity, final Class<? extends TemplatedClass<E>> m) throws MyException {
        this.counter += 1;
    }
}
        