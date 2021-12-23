
public class LoopTest {
    public static void main(String[] args) {
        int[] a = {1,2,3};
        int[] b = {4,5,6};
        int[] c = {7,8,9};
        for (int i : a) {
            if (i == 0) {
                break;
            } else {
                boolean fail = false;
                for (int j : b) {
                    boolean match = false;
                    for (int k : c) {
                        if (k == 42) {
                            match = true;
                        }
                    }
                    if (!match) {
                        fail = true;
                    }
                }
            }
        }
    }
}
        