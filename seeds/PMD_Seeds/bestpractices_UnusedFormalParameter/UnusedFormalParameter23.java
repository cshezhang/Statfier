
class Impl {
    public void m1() {
        class Local {
            private void m2(int arg1) { }
            private void m3() { m2(0); }
        }
        Local l = new Local();
        l.m3();
    }
    public void m4() {
        Object o = new Object() {
            private void m5(int arg1) { }
            public boolean equals(Object other) { m5(0); return false; }
        };
        o.equals(o);
    }
}
        