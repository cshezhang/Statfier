
import java.util.concurrent.Executors;

public class ExecutorsUsage {
    public void static_usage_of_Executors() {
         Executors.newSingleThreadExecutor().submit(new Runnable() {
             @Override
             public void run() {
             }
        });
        Executors.newFixedThreadPool(1).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }
}
        