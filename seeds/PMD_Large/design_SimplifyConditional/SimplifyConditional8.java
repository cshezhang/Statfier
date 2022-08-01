
public class Foo {
    public void bar(Object[] arguments) {
        if (
        arguments == null ||
        arguments.length < 3 || arguments.length > 4 ||
        !(arguments[0] instanceof String ) ||
        !(arguments[1] instanceof Boolean ) ||
        !(arguments[2] instanceof String ) ||
        arguments.length == 4 && !(arguments[3] instanceof String )
        ) {
            throw new IllegalArgumentException( "" );
        }
    }
}
        