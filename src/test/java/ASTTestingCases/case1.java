package ASTTestingCases;

/**
 * Description: test cases for AST Testing
 * Author: Austin Zhang
 * Date: 2021/10/12 1:15 下午
 */
public class case1 {

    public void testIfStatement(boolean tag) {
        if(tag)
            System.out.println(123);
        if(tag)
            System.out.println(123);
        else
            System.out.println(123);
        if(tag) {
            System.out.println("123");
        } else {
            System.out.println("123");
        }
        if(tag) {
            System.out.println(123);
            System.out.println(123);
        } else {
            System.out.println(123);
            System.out.println(123);
        }
    }

    public void testLoopStatement(boolean tag) {
        for(int i = 0; i < 10; i++)
            System.out.println(123);
        for(int i = 0; i < 10; i++) {
            System.out.println(123);
        }
        for(int i = 0; i < 10; i++) {
            System.out.println(123);
            System.out.println(123);
        }
        while(tag) {
            System.out.println();
        }
        while(tag) {
            System.out.println(123);
            System.out.println(123);
        }
        do
            System.out.println(123);
        while(tag);
        do {
            System.out.println(123);
        } while(tag);
        do {
            System.out.println(123);
            System.out.println(123);
        } while(tag);
    }

}
