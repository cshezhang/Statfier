
public class Foo {

    public int foo() {
        int a = 0; // overwritten in try body & catch
        try (Reader r = new StringReader("")) {
            a = r.read();  // overwritten in finally
        } catch (IOException e) {
            a = -1; // overwritten in finally
        } finally {
            a = 0;
        }
        return a;
    }

}        