
public class Foo {

    public int foo() {
        int a = 0; // used in catch
        try (Reader r = new StringReader("")) {
            a = r.read();  // used in finally
        } catch (IOException e) {
            a = -1; // used in finally
        } finally {
            print(a);
        }
        return 0;
    }

}        