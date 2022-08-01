
public class Foo {
    void printSomething() {
        try {
            System.out.println("Integers: ");
        } finally {
            Arrays.asList(0, 1, 2).map(new Function<Integer, Integer>() {
               @Override
               public Integer apply(Integer i) {
                   return i + 1;
               }
           }).forEach(i -> System.out.println(i));
        }
    }
}
        