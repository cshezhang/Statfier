
class Foo {
    void bar() {
        if (num == 0.0) {
            return MathExtItg.sgn0raw(num) == 1 ? IEEEclass.PositiveZero : IEEEclass.NegativeZero;
        }
    }
}
        