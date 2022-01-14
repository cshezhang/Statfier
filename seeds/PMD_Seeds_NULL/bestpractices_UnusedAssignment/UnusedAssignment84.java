
public class Foo {
    void bar(int i) {
        int j = 0;
        @SuppressWarnings("unused")
        int z = 0; // unused
        if (i < 10) {
            j = i;
        }
        System.out.println(j);
    }
}
        