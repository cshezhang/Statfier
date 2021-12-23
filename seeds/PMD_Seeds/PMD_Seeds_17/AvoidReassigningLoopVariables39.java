
public class Foo {
    void foo(int bar) {
        String[] strings = getStrings();
        for (String g : strings) {
            g = g.substring(0,g.lastIndexOf(":"));
            g = g + ": 1;"; // here
            FileUtil.writeLineToFile(w, g);
        }
        for (String item : strings) {
            item = item.trim();
            httpMethods = httpMethods | HttpMethods.getMethod(item);
        }
        for (String message : strings) {
            message = OutputFormatter.escape(message);
            lines.add(String.format(large ? "  %s  " : " %s ", message));
            len = Math.max(length(message) + (large ? 4 : 2), len);
        }


    }
}
        