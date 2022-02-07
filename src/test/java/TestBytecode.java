import org.junit.Test;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/19 7:35 下午
 */
public class TestBytecode {

    @Test
    public void foo1() {
        final String fs = "Not enough arguments %d and %d";  // a false negative to be reported  https://rules.sonarsource.com/java/type/Bug/RSPEC-2275
        String s = String.format("Not enough arguments %d and %d", 1);
        System.out.println(s);
    }
}
