
public class Escape {
    public static String escape(final String value)
    {
        StringBuilder s = new StringBuilder(value.length() + 2);
        s.append('"');
        for (int i = 0; i < value.length(); i++) {
            s.append(value.charAt(i));
        }
        s.append('"');
        return s.toString();
    }
}
        