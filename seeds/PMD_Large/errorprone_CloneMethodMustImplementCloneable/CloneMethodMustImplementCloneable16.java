
public class Outer {
    public static class Inner {
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
    public void foo() {
        class Local {
            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }
    }
}
        