
public class Foo {
    private static class NestedClass {
        boolean b0;
        boolean b = false; // Bad
        static boolean stb0;
        static boolean stb = false; // Bad
        Boolean stB = false;
        static Boolean B = false;

        double[] da0;
        double[] da = null; // Bad
        double[] da1 = new double[] { 0 };
        double[] da2 = computed();

        Object[][] oaa0;
        Object[][] oaa = null; // Bad
        Object[][] ooa1 = new Object[1][];
        Object[][] ooa2 = new Object[][] { { null } };

        private double[] computed() {
            double[] d = null;
            return d;
        }
    }
}
        