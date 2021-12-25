package ASTTestingCases.sub;

/**
 * Description: test cases for AST Testing
 * Author: Austin Zhang
 * Date: 2021/10/12 1:15 下午
 */
public class case1 {



    Object obj = new Object() {
        public void foo() {
            final String str = "123";
            System.out.println(str);
        }
    };

    enum enumClazz {
        RED;
        final String str = "123";
        public void foo() {
            System.out.println(str);
        }
    }

}
