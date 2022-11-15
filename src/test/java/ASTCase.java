import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/9/1 15:30
 */
public class ASTCase extends TestCase {

    public void foo() {
        if(1 == 2) {}
    }

//    public void lamCase() {
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);
//        for(int i = 0; i < list.size(); i++) {
//            int value = list.get(i);
//            System.out.println(value);
//        }
//        for(Integer i : list) {
//            System.out.println(i);
//        }
//        list.forEach(item -> System.out.println(item));
//        list.forEach(item -> {
//            System.out.println(item);
//            System.out.println(item);
//        });
//    }

}
