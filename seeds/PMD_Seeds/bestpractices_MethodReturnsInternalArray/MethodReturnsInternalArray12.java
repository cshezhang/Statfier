
public class WebServiceResponseMessage {
    private Object[] responseObjects;
    public Object[] getResponseObjects() {
        return responseObjects == null ? null : Arrays.copyOf(responseObjects, responseObjects.length);
    }
}
        