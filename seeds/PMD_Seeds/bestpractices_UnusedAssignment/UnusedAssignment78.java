
class Foo {
    private static final Method privateLookupInMethod;

    private static final Method lookupDefineClassMethod;

    private static final Method classLoaderDefineClassMethod;

    private static final ProtectionDomain PROTECTION_DOMAIN;

    private static final Throwable THROWABLE;

    private static final List<Method> OBJECT_METHODS = new ArrayList<Method>();

    static {
        Method privateLookupIn;
        Method lookupDefineClass;
        Method classLoaderDefineClass;
        ProtectionDomain protectionDomain;
        Throwable throwable = null;
        try {
            privateLookupIn = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    try {
                        return MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
                    }
                    catch (NoSuchMethodException ex) {
                        return null;
                    }
                }
            });
            lookupDefineClass = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    try {
                        return MethodHandles.Lookup.class.getMethod("defineClass", byte[].class);
                    }
                    catch (NoSuchMethodException ex) {
                        return null;
                    }
                }
            });
            classLoaderDefineClass = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    return ClassLoader.class.getDeclaredMethod("defineClass",
                                                               String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                }
            });
            protectionDomain = getProtectionDomain(ReflectUtils.class);
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    Method[] methods = Object.class.getDeclaredMethods();
                    for (Method method : methods) {
                        if ("finalize".equals(method.getName())
                            || (method.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) > 0) {
                            continue;
                        }
                        OBJECT_METHODS.add(method);
                    }
                    return null;
                }
            });
        }
        catch (Throwable t) {
            privateLookupIn = null;
            lookupDefineClass = null;
            classLoaderDefineClass = null;
            protectionDomain = null;
            throwable = t;
        }
        privateLookupInMethod = privateLookupIn;
        lookupDefineClassMethod = lookupDefineClass;
        classLoaderDefineClassMethod = classLoaderDefineClass;
        PROTECTION_DOMAIN = protectionDomain;
        THROWABLE = throwable;
    }

}
        