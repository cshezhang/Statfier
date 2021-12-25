
class Foo {
    Foo() {
        boolean a, b;
        int j = 23;
        switch(j) {
            case 1:
            case 2: break;
            case 3: j = 5; break;
            case 4: if (b && a) { bar(); } break;
            default: break;
        }
    }
}
        