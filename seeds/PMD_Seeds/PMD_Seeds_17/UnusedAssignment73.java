
class Foo {

    int f1 = 0;
    int f2 = 0;

    Foo(int f1) {
        this.f1 = f1;
    }

    Foo(int f1, int g) {
        this.f1 = f1;
        this.f2 = f1 + g;
    }

}
        