package iter0;

class Foo {
    List<String> bar() {
        return new ArrayList<String>(){{addAll("a","b","c");}};
    }
}
        