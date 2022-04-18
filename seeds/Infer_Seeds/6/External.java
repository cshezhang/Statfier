

import codetoanalyze.java.SomeExternalClass;

public class External {
    /* This function should raise deduplicated issues because the symbolic value of external method
     * should be instantiated to top. */
    void external_function_Bad(SomeExternalClass v) {
        int i = (int) (v.externalMethod1()) + 1 + (int) (v.externalMethod1()) + 1;
    }

    /* This function should have no proof obilgation. */
    void call_external_function_Good(SomeExternalClass v) {
        external_function_Bad(v);
    }
}
