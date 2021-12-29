
public class Test {
    private String str1;

    public String themeName() {
        return (Character.toUpperCase(str1.charAt(0))
                + str1.substring(1)).replace('_', ' ');
    }
}
        