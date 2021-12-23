

class Foo {
    public Object intercept(Object proxy) throws Throwable {
        Object oldProxy = null; // FP here
        try {
            oldProxy = new Object[] { proxy };
            return null;
        }
        finally {
            System.out.println(oldProxy);
        }
    }
}
        