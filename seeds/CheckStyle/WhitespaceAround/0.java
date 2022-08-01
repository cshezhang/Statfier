

class Test {
    public Test(){} // 2 violations, '{' is not followed and preceded by whitespace.
    public static void main(String[] args) {
        if (foo) { // ok
            // body
        }
        else{ // violation
            // body
        }

        for (int i = 1; i > 1; i++) {} // violation, '{' is not followed by whitespace.

        Runnable noop = () ->{}; // 2 violations,
                                 // '{' is not followed and preceded by whitespace.
        try {
            // body
        } catch (Exception e){} // 2 violations,
                                // '{' is not followed and preceded by whitespace.

        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        for (char item: vowels) { // ok, because ignoreEnhancedForColon is true by default
            // body
        }
    }
}
        