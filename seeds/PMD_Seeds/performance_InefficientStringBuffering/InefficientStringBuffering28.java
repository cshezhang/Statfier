
public class Foo {
    private int someInt = 1;
    public String toString() {
        StringBuilder sb = new StringBuilder("Foo{");
        sb.append("someInt=").append(this.someInt < 0 ? "n/a" : this.someInt + "ms");
        sb.append("someInt2=").append(this.someInt >= 0 ? this.someInt + "ms" : "n/a");
        sb.append('}');
        return sb.toString();
    }
}
        