
public class C0 {
    class Unresolved {
        private final long val;
        public long getVal() { return val; }
    }

    {
        if (c1.getVal() != c2.getVal()) {  // <-- here
        }
    }
}
        