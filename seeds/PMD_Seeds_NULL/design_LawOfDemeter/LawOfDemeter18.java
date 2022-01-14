
public final class Util {
    public static boolean check(String passwd, String hashed) {
        try {
            final String[] parts = hashed.split("\\$");

            if (parts.length != 5 || !parts[1].equals("s0")) {      // wrong violation - method chain calls
                throw new IllegalArgumentException("Invalid hashed value");
            }
        } catch (Exception e) { }
    }
}
        