
public class UnusedPrivateMethodFP {
    private void print(String s) { // <- unused private method?
        System.out.println(s);
    }

    public void run() {
        print(new Integer(1).toString()); // it is used here
    }

    private void print2(String s) {
        System.out.println(s);
    }

    public void run2() {
        String temp = new Integer(1).toString();
        print2(temp); // workaround with extra temporary variable
    }

    private void print3(String s) {
        System.out.println(s);
    }

    public void run3() {
        print3((String)new Integer(1).toString()); // workaround with extra cast
    }

    public void runBoolean(String s) {
        privateBooleanMethod(s, "true".equals(s));
    }

    private void privateBooleanMethod(String s, boolean isTrue) {
        System.out.println(s);
    }
}
        