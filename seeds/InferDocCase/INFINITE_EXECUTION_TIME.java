public class INFINITE_EXECUTION_TIME {
    // Expected: square root(x), got T
    void square_root_FP(int x) {
        int i = 0;
        while (i * i < x) {
            i++;
        }
    }
}