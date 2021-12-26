

class Foo {
    int foo(int index, int[] arr) {
        index.method().field = 4; // not an assignment to index
    }

}
        