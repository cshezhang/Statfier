
public class UseStringBuffer {
    public void foo() {
        String country = "";
        country = (country == null || "".equals(country))
                ? ((String) getCountry())
                : country;
    }
    public void foo2() {
        String country = request.getParameter("country");
        country = (country == null) ? "USA" : country;
    }
}
        