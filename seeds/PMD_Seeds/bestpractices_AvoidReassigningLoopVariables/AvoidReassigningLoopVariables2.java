
public class Foo {
    private int[] array = new int[10];

    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            this.array[i] = 5;
            array[i] = 5;
            this.array[i]++;
            array[i]++;
            ++this.array[i];
            ++array[i];
            this.array[i] += 1;
            array[i] += 1;
        }
    }
}
        