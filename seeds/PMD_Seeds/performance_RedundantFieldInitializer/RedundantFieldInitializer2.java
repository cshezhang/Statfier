
public class Foo {
    boolean[] ba0;
    boolean[] ba = null; // Bad
    boolean[] ba1 = new boolean[] { false };
    boolean[] ba2 = computed();

    byte[] bya0;
    byte[] bya = null; // Bad
    byte[] bya1 = new byte[] { 0 };

    short[] sa0;
    short[] sa = null; // Bad
    short[] sa1 = new short[] { 0 };

    char[] ca0;
    char[] ca = null; // Bad
    char[] ca1 = new char[] { 0 };

    int[] ia0;
    int[] ia = null; // Bad
    int[] ia1 = new int[] { 0 };

    float[] fa0;
    float[] fa = null; // Bad
    float[] fa1 = new float[] { 0 };

    double[] da0;
    double[] da = null; // Bad
    double[] da1 = new double[] { 0 };

    static boolean[] computed() {
        return null;
    }
}
        