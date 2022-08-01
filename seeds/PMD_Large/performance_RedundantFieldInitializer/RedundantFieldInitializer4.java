
public class Foo {
    int i1, iam1[];
    int i2 = 0, iam2[]; // Bad
    int i3, iam3[] = null; // Bad
    int i4 = 0, iam4[] = null; // 2 Bad
    int i5 = 0, iam5[] = computed(); // Bad

    static int[] computed() {
        return null;
    }
}
        