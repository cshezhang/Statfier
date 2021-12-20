
package iter0;

public class DirectSynchronizingSubclass extends BaseClass {

    @Override
    protected synchronized void doBase() {
        // overriding for synchronized
        super.doBase();
    }
}
        