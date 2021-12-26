
public class Foo {
    @SuppressWarnings("unused")
    void bar(int i) {
        int j = 0;
        int z = 0; // unused
        if (i < 10) {
            j = i;
        }
        System.out.println(j);
    }
}
        