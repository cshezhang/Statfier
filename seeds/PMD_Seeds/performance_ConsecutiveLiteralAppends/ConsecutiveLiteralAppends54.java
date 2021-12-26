
public class ConsecutiveLiteralAppendsFP {
    public String test() {
        StringBuilder builder = new StringBuilder("[");
        nodes.forEach((k, v) -> builder
                .append(k)
                .append(" = ")
                .append(valueToStringFunction.apply(v))
                .append(", "));
        int length = builder.length();
        if (length > 1) {
            builder.delete(length - 2, length);
        }
        builder.append(']');
        return builder.toString();
    }
}
        