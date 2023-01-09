
public class Foo {
    public void run1(String ...args) {
        int i;
        for (String a : args) {
            String id = a + " -> " + i++;
            System.out.println(id);
        }
    }
    public void run2(String ...args) {
        int x;
        for (String a : args) {
            String id = a + " -> " + (++x);
            System.out.println(id);
        }
    }
}
        