

class Test {
    public static void main(String[] args) {
        for (int i = 100;i > 10; i--){} // ok
        do {} while (i = 1); // ok
        int a=4; // 2 violations, '=' is not followed and preceded by whitespace.
    }
}
        