
public class Foo {

    private static boolean isInstanceOf(final MBeanServer mbs,
                                        final ObjectName name,
                                        final String className) {
        PrivilegedExceptionAction<Boolean> act =
            new PrivilegedExceptionAction<Boolean>() {
                public Boolean run() throws InstanceNotFoundException {
                    return mbs.isInstanceOf(name, className);
                }
            };
        try {
            return AccessController.doPrivileged(act);
        } catch (Exception e) {
            logger.fine("isInstanceOf", "failed: " + e);
            logger.debug("isInstanceOf", e);
            return false;
        }
    }
}
        