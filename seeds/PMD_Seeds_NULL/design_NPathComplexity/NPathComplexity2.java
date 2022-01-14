
class Foo {
    void bar() {
        boolean a, b;
        try {
            switch(j) { // 7
            case 1:
            case 2: break;
            case 3: j = 5; break;
            case 4: if (b && a) { bar(); } break;
            default: break;
            }

            switch(j) { // * 7
            case 1:
            case 2: break;
            case 3: j = 5; break;
            case 4: if (b && a) { bar(); } break;
            default: break;
            }

            if (true || a || b); // * 4

        } catch (ScaryException e) {
            if (true || a); // + 3
        }
    }
}
        