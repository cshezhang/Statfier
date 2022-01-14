
public class Foo {
    public void example() {
        List<String> list = new ArrayList<String>();
        list.add("Tata");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
                String s = it.next();
                System.out.println(s);
        }
        if (list != null && !list.isEmpty()) {
            for (String s : list) {
                if (!s.isEmpty()) {
                    System.out.println(s);
                }
            }
        }

        List<String> anotherList = calcList();
        for (String s : anotherList) {
            if (!s.isEmpty()) {
                System.out.println(s);
            }
        }
    }
}
        