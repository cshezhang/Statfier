/**
 * Description:
 * Author: Vanguard
 * Date: 2022/9/1 15:30
 */
public class ASTCase {
    private int x;  // no reason to exist at the Foo instance level
    public int foo(int y) {
        x = y + 5;
        return x;
    }
}