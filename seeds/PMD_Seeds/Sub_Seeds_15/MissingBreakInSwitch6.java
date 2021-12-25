
public class Foo {
    public static final int x1 = 1;
    public static final int x2 = 2;
    public static final int x3 = 3;

    public Object bar(int x, boolean condition) {
        switch (x)
        {
            case x1:
            case x2:
                if (condition)
                    return new Object();
                else
                    return null;
            case x3:
                if (condition)
                    return new Object();
                else
                    return null;
            default:
                return null;
        }
    }
}
        