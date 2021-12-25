
public class UnnecessaryCastSample {
    public void localVars() {
        List<String> strings = new ArrayList<>();
        List<String> copy = (List<String>) strings.clone();
    }
}
        