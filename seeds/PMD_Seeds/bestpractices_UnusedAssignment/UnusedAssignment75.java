
class Foo {

    int f1 = 0;
    int f3 = 0;

    Foo(int f) {
        f1 = f;
    }

    {
        f1 = 1;
    }

    Foo(int f, int g) {
        f1 = f;
        f2 = f + g;
    }

}
        