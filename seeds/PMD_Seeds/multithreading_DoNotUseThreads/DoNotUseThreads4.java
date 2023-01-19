import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUsage {
  public void executor_service_method_invocation() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(
        new Runnable() {
          @Override
          public void run() {}
        });
  }
}

