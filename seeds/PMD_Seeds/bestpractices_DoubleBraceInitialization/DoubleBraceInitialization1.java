
class Foo {
    List<String> bar() {
        return new ArrayList<String>() {
            {addAll("a","b","c");}

            void add(String x) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
        