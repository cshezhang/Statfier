
public class UseProperClassLoaderFN {
    {
        Object o = new Object();
        ClassLoader cl = o.getClass().getClassLoader();

        Class<?> c = o.getClass();
        ClassLoader cl2 = c.getClassLoader();
    }
}
        