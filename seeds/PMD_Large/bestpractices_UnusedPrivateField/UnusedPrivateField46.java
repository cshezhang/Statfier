
public class Foo {
    private int counter;
    public void skip(int n) {
        while (counter++ < n) {
            System.out.println("Skipping");
        }
    }
}
        