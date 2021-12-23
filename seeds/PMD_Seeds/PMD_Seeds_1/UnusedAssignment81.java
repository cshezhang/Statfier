

class Foo {
    int foo(int index, String[] arr) {
        arr = new String[] { "1" };
        arr[0].trim(); // this is a usage of arr
    }

}
        