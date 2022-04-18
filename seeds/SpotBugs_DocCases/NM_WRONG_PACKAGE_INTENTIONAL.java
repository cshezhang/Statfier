class alpha {
    public static class Foo {}
}

class beta {
    public static class Foo {}
}

class A {
    public int f(alpha.Foo x) { return 17; }
}

class B extends A {
    public int f(alpha.Foo x) { return 42; }
    public int f(beta.Foo x) { return 27; }
}