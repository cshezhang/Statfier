
import java.util.*;

final class Test {
    private Test() { }

    private static class Locale {
        public final java.util.Locale locale; // Here we need to fully qualify
        public Locale(final String tag) {
            this.locale = java.util.Locale.forLanguageTag(tag);
        }
    }

    public static void main(String[] args) {
        final Locale l = new Locale("fr-CA");
        System.out.println(l.toString());
    }
}
        