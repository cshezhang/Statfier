

import codetoanalyze.java.annotation.NonBlocking;
import codetoanalyze.java.annotation.SuppressLint;

public class STARVATION {

    @SuppressLint("STARVATION")  // avoid report
    public void m1() {
    }


    @NonBlocking
    public void m2() {
    }

}