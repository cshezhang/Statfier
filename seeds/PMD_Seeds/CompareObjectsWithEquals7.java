
package iter0;

public class CompareObjectsWithEqualsSample {
    void array(int[] a, String[] b) {
        if (a[1] == b[1]) {} // int == String - this comparison doesn't make sense
    }
    void array2(int[] c, int[] d) {
        if (c[1] == d[1]) {}
    }
    void array3(String[] a, String[] b) {
        if (a[1] == b[1]) {} // not ok
    }
}
        