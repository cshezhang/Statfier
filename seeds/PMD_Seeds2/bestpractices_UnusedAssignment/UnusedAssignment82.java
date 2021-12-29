

class Foo {
    int foo(int index, String[] arr) {
        arr = new String[] { "1" };
        arr.clone().clone(); // this is a usage of arr
    }

}
        