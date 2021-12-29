
public class CustomerErrorCollector {

    private final ConcurrentHashMap<String, String> customerErrors = new ConcurrentHashMap<>();

    public void error(String customerNr, String errorMsg) {
        customerErrors.put(customerNr, errorMsg);
    }

    public Map<String, String> getAndReset() {
        final Map<String, String> copy = new HashMap<>(customerErrors);
        customerErrors.clear();
        return copy; // PMD complains that variable could be avoided
    }
}
        