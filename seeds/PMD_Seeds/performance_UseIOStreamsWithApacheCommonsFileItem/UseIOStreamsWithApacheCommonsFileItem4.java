
public class Foo {
    private String getString(final HarmlessClass harmless) {
        return harmless.get(); // harmless
    }

    private class HarmlessClass {
        public byte[] get() {
            return new byte[0];
        }
    }
}
        