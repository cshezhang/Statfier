
public class MyClass {
    void loop(List<String> l) {
        List<String> l2 = new ArrayList<>(l);
        for (int i = 0; i < l.size(); i++) {
            System.out.println(l2.get(i));
        }
    }
}
        