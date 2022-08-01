
class StringPropertyTest {
    private String newString() {
        int strLength = randomInt(1, MAX_STRING_LENGTH);

        char[] chars = new char[strLength];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = randomCharIn(CHARSET);
        }
        return new String(chars);
    }
}
        