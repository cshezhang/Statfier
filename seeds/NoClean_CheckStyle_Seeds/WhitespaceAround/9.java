

class Test {
    public static void main(String[] args) {
        int a=4; // 2 violations , '=' is not followed and preceded by whitespace.
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        for (char item: vowels) { // violation, ':' is not preceded by whitespace.
            // body
        }
    }
}
        