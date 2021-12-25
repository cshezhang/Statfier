
public class MyClass {
    void loop(List<String> l) {
        for (int i = 1; i < filters.size(); i++) {
            builder.append(' ').append(getOperator()).append(' ');
            builder.append(filters.get(i));
        }
    }
}
        