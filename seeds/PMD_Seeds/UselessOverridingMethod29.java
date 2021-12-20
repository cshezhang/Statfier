
package iter0;

public class DirectSubclass2 extends DirectSubclass {
    @Override
    public void doBase() { // it's already public in DirectSubclass
        super.doBase();
    }
}
        