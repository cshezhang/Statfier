
public class Test {
    public void method(String annotationType, Map<String, Object> attributes) {
        boolean isStereotype = annotationType.equals("javax.inject.Named");
        if (isStereotype && attributes != null && attributes.containsKey("value")) {}
        if (isStereotype || attributes == null || attributes.containsKey("value")) {}

        if (isStereotype && attributes.containsKey("value") && attributes != null) {}
        if (isStereotype || attributes.containsKey("value") || attributes == null) {}
    }
}
        