
public class Foo {
   public String bar(Object o) {
        return ((Exception) o).getMessage();
   }
}
        