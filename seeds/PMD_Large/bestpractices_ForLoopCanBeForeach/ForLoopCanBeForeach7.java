
class Foo {
    protected static final char[] filter(char[] chars, char removeChar) {
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == removeChar) {
                count++;
            }
        }

        char[] results = new char[chars.length - count];

        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != removeChar) {
                results[index++] = chars[i];
            }
        }
        return results;
    }
}
        