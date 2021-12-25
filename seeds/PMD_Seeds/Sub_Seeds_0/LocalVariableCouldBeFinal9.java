
/**
 * See: https://sourceforge.net/tracker/?func=detail&aid=2614040&group_id=56262&atid=479921
 */
public class Foo {
    public String bar() {
        int count = 1;
        final String wrong = String.valueOf( count += 2); // 'count' is used so it's a false +
        return wrong;
    }
}
        