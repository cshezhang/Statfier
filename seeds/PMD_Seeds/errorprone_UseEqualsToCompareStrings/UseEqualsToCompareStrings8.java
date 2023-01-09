
public class O {
    boolean f(String s) {
        final Matcher matcher = null;
        if (matcher.matches()) {

            final var firstString = matcher.group("a");
            final var secondString = matcher.group("b");

            if (firstString.isEmpty() != secondString.isEmpty()) { // <- violation
                // ...
            }
        }
    }
}
        