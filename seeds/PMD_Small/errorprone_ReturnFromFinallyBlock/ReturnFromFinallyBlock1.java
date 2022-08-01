
public class Foo {
    String getBar() {
        try {
            return "buz";
        } catch (Exception e) {
            return "biz";
        } finally {
            return "fiddle!"; // bad!
        }
    }
}
        