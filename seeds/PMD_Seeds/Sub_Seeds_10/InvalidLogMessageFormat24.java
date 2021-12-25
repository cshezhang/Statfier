
public class NullPointerTypeProblem {
    public void foo() {
        java.util.StringTokenizer st = new java.util.StringTokenizer("a.b.c.d", ".");
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}
        