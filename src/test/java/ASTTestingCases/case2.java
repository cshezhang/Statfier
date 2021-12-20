package ASTTestingCases;

/**
 * Description: Test cases for AST testing about If / Loop - nested blocks
 * Author: Austin Zhang
 * Date: 2021/10/12 1:21 下午
 */
public class case2 {

    public void testEnhancedLoop(boolean tag) {
        String[] strs = new String[5];
        strs[0] = "0";
        strs[1] = "1";
        strs[2] = "2";
        strs[3] = "3";
        strs[4] = "4";
        for(String s : strs) {
            System.out.println(s);
        }
    }

    public void testNest(boolean tag) {
        while(tag) {
            if(tag) {
                if(tag) {
                    System.out.println(123);
                } else {
                    System.out.println(456);
                }
            }
            do
                if(tag) {
                    if(tag) {
                        if(tag)
                            System.out.println(123);
                    }
                }
            while(tag);
        }
        if(tag) {
            while(tag) {
                System.out.println(123);
            }
        } else {
            do {
                System.out.println(123);
            } while(tag);
        }
    }

}
