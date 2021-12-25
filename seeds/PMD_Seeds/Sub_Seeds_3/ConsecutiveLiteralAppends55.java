
public class ConsecutiveLiteralAppendsFN {
    public void test() {
        String resourceDescription = "Foo";
        StringBuilder result = new StringBuilder(resourceDescription.length() + 16);
        StringBuilder result2 = new StringBuilder();
        for (int i = 0; i < resourceDescription.length(); i++) {
            char character = resourceDescription.charAt(i);
            if (character == '\\') {
                result.append('/');
            }
            else if (character == '"') {
                result.append("\\").append('"'); // violation: 2 literals (String + Char) can be combined
                result2.append(',').append(' '); // violation: 2 literals (Char + Char) can be combined
            }
            else {
                result.append(character);
            }
        }
    }
}
        