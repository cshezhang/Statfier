
class ClassWithOnlyPrivateConstructorsShouldBeFinal {
    private String string;

    ClassWithOnlyPrivateConstructorsShouldBeFinal(final Object object) {
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
        