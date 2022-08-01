
class Foo {
    List<String> bar() {
        return new ArrayList<String>(){

            {addAll("a","b","c");}

            int field;
        };
    }
}
        