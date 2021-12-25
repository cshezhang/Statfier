
public class Foo {
    public void bar() {
        String[] arr = getArray();
        String s = new String(arr[0]);
        // better
        String s2 = arr[0];
    }
    public void bar2() {
        String[][] arr = getArray2();
        String s = new String(arr[0][0]);
    }
}
        