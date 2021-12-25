
public class Foo {
    private String[] array = new String[10];

    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            this.array[i++] = "Number " + i;
            this.array[++i] = "Number " + i;
            this.array[i = 5] = "Number " + i;
            array[i++] = "Number " + i;
            array[++i] = "Number " + i;
            array[i = 5] = "Number " + i;
        }
    }
}
        