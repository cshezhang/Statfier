package iter0;

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

        java.lang.Object[][] oaa0;
        java.lang.Object[][] oaa = null; // Bad
        java.lang.Object[][] ooa1 = new java.lang.Object[1][];
        java.lang.Object[][] ooa2 = new java.lang.Object[][] { { null } };

        private double[] computed() {
            double[] d = null;
            return d;
        }
    }
}
        