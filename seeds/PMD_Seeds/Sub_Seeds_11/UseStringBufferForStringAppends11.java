
public class Foo {
    public void bar() {
        String country = request.getParameter("country");
        country = (country != null) ? country : "USA";
    }
}
        