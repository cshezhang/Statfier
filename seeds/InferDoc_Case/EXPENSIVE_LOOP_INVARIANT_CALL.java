public class EXPENSIVE_LOOP_INVARIANT_CALL {
    int incr(int x) {
        return x + 1;
    }

    // incr will not be hoisted since it is cheap(constant time)
    void foo_linear(int size) {
        int x = 10;
        for (int i = 0; i < size; i++) {
            incr(x); // constant call, don't hoist
        }
    }

    // call to foo_linear will be hoisted since it is expensive(linear in size).
    void symbolic_expensive_hoist(int size) {
        for (int i = 0; i < size; i++) {
            foo_linear(size); // hoist
        }
    }
}