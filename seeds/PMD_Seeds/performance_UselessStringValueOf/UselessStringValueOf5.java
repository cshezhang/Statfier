
public class Foo {
    private String getMessage(Object pPassedValue) {
        return "someString" + (pPassedValue == null ? "null" : '\'' + String.valueOf(pPassedValue) + '\'') + "end of my string";
    }
}
        