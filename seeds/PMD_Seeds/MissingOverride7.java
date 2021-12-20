
package iter0;
public class AnonClassExample {
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bar();
            }

            public void bar() {

            }
        }).start();
    }
}
        