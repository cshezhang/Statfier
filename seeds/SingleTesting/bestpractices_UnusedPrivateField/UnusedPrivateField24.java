
public class XPathFunctionContext {

    private class Singleton {
        private String foo = "";
    }

    public String getFoo() {
        return (new Singleton()).foo;
    }
}
        