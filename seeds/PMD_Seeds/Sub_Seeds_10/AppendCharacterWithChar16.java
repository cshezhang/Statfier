
public class Foo {
    private String name = "bar";
    public String toString() {
        StringBuilder sb = new StringBuilder("#").append("Foo").append('#').append(this.name);
        return sb.toString();
    }
}
        