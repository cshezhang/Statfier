
public class MyClass {
    void loop(List<String> l) {
        for (int i = l.size() - 1; i > 0; i-= 1) {
            System.out.println(i + ": " + l.get(i));
        }
    }
}
        