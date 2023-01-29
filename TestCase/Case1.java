public class Foo {
    public void method() {
        String[] sa = {"a", "b"};
        for (String s : sa) {}
        for (String s : sa) {
            s = "new string"; // violation
        }
        for(int i = 0; i < sa.size(); i++) {
            s = "123";
        }
    }
}

