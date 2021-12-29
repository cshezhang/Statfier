
public class UnnecessaryCastSample {
    public void localVars() {
        List<Number> numbers = Arrays.asList(1, 2, 3);
        Integer myInt = (Integer) numbers.get(0);

        List<Object> data = new ArrayList<>();
        String item = (String) data.get(0);

        Map<String, ?> map = new HashMap<>();
        String dataFromMap = (String) map.get("foo");
    }
}
        