import com.google.common.base.Preconditions;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/19 7:35 PM
 */
public class CaseTest {

    Object x;

    public void test0(Object x) {
        Preconditions.checkNotNull("x should be nonnull");
        this.x = x;
    }

}
