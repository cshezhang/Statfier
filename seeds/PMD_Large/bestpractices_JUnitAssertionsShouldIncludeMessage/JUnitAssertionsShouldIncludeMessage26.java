
public class Foo {
    public void test1() {
        assertEquals(1, 1);
    }
    private void assertEquals(int x, int y) {
        if (x != y) throw new AssertionError();
    }
}
        