
package pmdtests;

class TaskManager {
    private final List<ForkJoinTask<Integer>> tasks;

    public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
        for (ForkJoinTask<Integer> task : tasks) {
            if (!task.equals(cancelTask)) {
                task.cancel(true);
                ((SearchNumberTask) task).writeCancelMessage(); // wrong violation: method chain calls
            }
        }
    }
}
        