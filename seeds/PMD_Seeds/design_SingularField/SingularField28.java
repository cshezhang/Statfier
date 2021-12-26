
public enum MyEnum {
    A("a"),
    B("b");

    private final String description; // <-- SingularField reported

    private MyEnum(String description) {
        this.description = description;
    }

    public static MyEnum byDescription(String description) {
        for (MyEnum myEnum : values()) {
            if (myEnum.description.equals(description)) { // <--- USED HERE
            return myEnum;
            }
        }
        return null;
    }
}
        