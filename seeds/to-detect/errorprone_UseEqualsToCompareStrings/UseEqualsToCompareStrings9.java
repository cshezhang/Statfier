
public class O {
    boolean f(String s) {
        final Matcher matcher = null;
        if (matcher.matches()) {

            final String firstString = matcher.group("a");
            final String secondString = matcher.group("b");

            if (firstString.isEmpty() != secondString.isEmpty()) { // <- violation
                // ...
            }
        }
    }
}
        