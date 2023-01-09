
public class Foo {
    String getBar() {
        try {
        } finally {
            Collection<ServiceExecutionEntry> untracked = serviceExecutionTracker.untrackMatchingEntries(e -> {
                ServiceExecutionIdentifiers ids = e.getIdentifiers();
                Long execBqiId = (ids == null) ? null : ids.getBqiId();
                return Objects.equals(bqiId, execBqiId);
            });
            untracked.forEach(e -> logger.info("overwriteLastBqi(bqId={}, bqiId={}) untracked {}", bqId, bqiId, e));
        }
    }
}
        