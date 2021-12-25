
public enum Foo {
    A {
        { setCycleDuration(Duration.millis(1200)); }
    };


    { setCycleDuration(Duration.millis(1200)); }

}
        