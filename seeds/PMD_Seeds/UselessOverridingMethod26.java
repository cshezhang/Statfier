
package iter0;

public class DirectSubclass extends BaseClass {

    @Override
    public void doBaseWithArg(String foo) {
        super.doBaseWithArg(foo.toString());
    }
}
        