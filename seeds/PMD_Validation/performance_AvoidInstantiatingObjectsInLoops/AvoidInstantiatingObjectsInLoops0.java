
public class Foo {
    public void test() {
        ArrayList<Obejct> strs = new ArrayList<>();
        for(int i = 0; i < strs.size(); i++) {
            Foo foo = new Foo();  // should report a warning here
        };
    }
}
        