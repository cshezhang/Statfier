
public class XPathFunctionContext {

    private static class Singleton {
        private static XPathFunctionContext instance = new XPathFunctionContext();
    }

    public static XPathFunctionContext getInstance() {
        return Singleton.instance;
    }
}
        