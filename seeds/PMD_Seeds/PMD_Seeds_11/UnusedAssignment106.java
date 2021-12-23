
            public class Test {
                public int indexOf(Object obj) {
                    // throws ClassCastException
                    Integer dig = (Integer)obj;
                    if (dig != 0 && dig != 1) {
                        return -1;
                    }
                    // throws IllegalArgumentException and NullPointerException
                    boolean cand = int2bool(dig);//<cand
                    for(int i = 0; i < this.size; i++) {
                        if (cand == this.digits[i]) {
                            return i;
                        }
                    }
                    return -1;
                }
            }
        