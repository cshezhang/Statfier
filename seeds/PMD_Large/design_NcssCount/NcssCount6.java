
public enum EnumWithAnonymousInnerClass {
    A {
        @Override
        public void foo() {
            super.foo();
        }
    },
    B;

    public void foo() {
    }

    interface Inner {
        int get();
    }

    public static final Inner VAL = new Inner() {
        @Override
        public int get() {
            return 1;
        }
    };
}
        