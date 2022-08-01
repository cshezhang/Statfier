
public class Foo {
    char a1 = 0, a2 = 0; // 2 Bad
    char a3 = 0, a4; // Bad
    char a5, a6 = 0; // Bad
    char a7 = computed(), a8 = 0, a9; // Bad

    static char computed() {
        return 0;
    }
}
        