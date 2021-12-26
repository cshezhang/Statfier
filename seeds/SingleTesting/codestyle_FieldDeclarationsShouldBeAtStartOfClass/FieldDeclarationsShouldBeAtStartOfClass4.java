
public class Foo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Foo.class);

    public static enum MyType {
        ABC, DEF, GHI, JHK
    };

    private int id;
    private MyType myType;
    private String name;

    // OK, now constructors, getters, setters, etc.
}
        