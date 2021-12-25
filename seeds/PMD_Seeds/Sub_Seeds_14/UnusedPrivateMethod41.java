
public class TestPrivate<T> {
    protected Object getProtected(final T bean) {
        return getPrivate(bean);
    }
    private Object getPrivate(final Object bean) {
        return bean;
    }
}
        