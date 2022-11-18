

class Test {
    public static void main(String[] args) {
        Runnable noop = () -> {}; // ok
        int a=4; // 2 violations, '=' is not followed and preceded by whitespace.
    }
}
        