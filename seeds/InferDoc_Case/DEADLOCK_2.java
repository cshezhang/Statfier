import java.util.Vector;

public class DEADLOCK_2 {

    Object lockA = new Object();
    Vector vector = new Vector();

    public void lockAThenAddToVector() {
        synchronized (lockA) {
            vector.add(new Object());
        }
    }

    public void lockVectorThenA() {
        synchronized (vector) {
            synchronized (lockA) {
                // do something with both resources
            }
        }
    }
}