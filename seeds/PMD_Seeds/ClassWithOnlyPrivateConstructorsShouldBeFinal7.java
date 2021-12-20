package iter0;

class ClassWithOnlyPrivateConstructorsShouldBeFinal {
    private String string;

    ClassWithOnlyPrivateConstructorsShouldBeFinal(final java.lang.Object object) {
        this(object.toString());
    }

    private ClassWithOnlyPrivateConstructorsShouldBeFinal(final String string) {
        super();

        setString(string);
    }

    public final void setString(final String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
        