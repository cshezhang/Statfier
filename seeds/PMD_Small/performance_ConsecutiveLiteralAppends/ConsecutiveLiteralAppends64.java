
public class FalsePositive {

    public static String escapeHTML(String text) {
        int length = text.length();
        int index = findHTMLReservedChar(text);
        if (index == length) return text;
        var builder = new StringBuilder(length * 2); // Rule:InsufficientStringBufferDeclaration Priority:3 StringBuffer constructor is initialized with size 16, but has at least 29 characters appended..
        for (int i = 0; i < index; i++) builder.append(text.charAt(i));
        for (; index < length; index++) {
            char ch = text.charAt(index);
            switch (ch) {
                case '<' -> { builder.append("&lt;"); } // Rule:ConsecutiveLiteralAppends Priority:3 StringBuffer (or StringBuilder).append is called 6 consecutive times with literals. Use a single append with a single combined String..
                case '>' -> builder.append("&gt;");
                case '"' -> builder.append("&quot;");
                case '&' -> builder.append("&amp;");
                case '\'' -> builder.append("&#39;");
                case '/' -> builder.append("&#47;");
                default -> builder.append(ch);
            }
        }
        return builder.toString();
    }

    private static int findHTMLReservedChar(String text) {
        return 0;
    }
}
        