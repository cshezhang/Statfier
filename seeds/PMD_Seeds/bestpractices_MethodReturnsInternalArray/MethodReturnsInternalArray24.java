
public enum MethodReturnsInternalArrayCaseEnum {
    ONE("One"),
    TWO("Two", "Three");

    private String[] titles;

    MethodReturnsInternalArrayCaseEnum(String... titles) {
        this.titles = titles;
    }

    public String[] getTitles() {
        return titles;
    }
}
        