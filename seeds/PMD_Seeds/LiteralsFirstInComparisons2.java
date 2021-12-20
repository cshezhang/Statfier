package iter0;

public class Foo {
    void bar() {
        if ((str == null) || (str.equals(""))) {
            str = "snafu";
        }
        if (str == null || str.equals("")) {
            str = "snafu";
        }
        if ((str != null) && (str.equals(""))) {
            str = "snafu";
        }
        if (str != null && str.equals("")) {
            str = "snafu";
        }
    }
}
        