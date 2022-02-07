package MutatorTestingCases;

import java.util.Collection;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/20 4:16 下午
 */
public class Case14 {

    String[] getAsArray(Collection<String> c) {
        return (String[]) c.toArray();
    }

}
