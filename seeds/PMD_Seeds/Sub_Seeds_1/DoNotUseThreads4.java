
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ExecutorServiceUsage {
    public void executor_service_method_invocation() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
             @Override
             public void run() {
             }
        });
    }
}
        