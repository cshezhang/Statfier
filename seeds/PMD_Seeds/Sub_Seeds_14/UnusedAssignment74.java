
class Foo {

    Bar f1 = 0;
    Bar f2 = 0;

    Foo(Bar f1) {
        this.f1.field = f1;
    }

    Foo(Bar f1, Bar g) {
        this.f1 = f1;
        this.f2 = f1 + g;
    }

}
        