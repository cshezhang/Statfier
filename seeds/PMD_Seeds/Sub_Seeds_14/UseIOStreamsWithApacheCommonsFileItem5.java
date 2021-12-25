
public class Foo {
    private String getString(final HarmlessClass harmless) {
        return harmless.getString(); // harmless
    }

    private class HarmlessClass {
        public String getString() {
            return "";
        }
    }
}
        