public class WL_USING_GETCLASS_RATHER_THAN_CLASS_LITERAL {
    
    private static final String base = "label";
    private static int nameCounter = 0;

    String constructComponentName() {
        synchronized (getClass()) { // should use Label.class
            return base + nameCounter++;
        }
    }
}