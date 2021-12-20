package ASTTestingCases;

import java.util.HashMap;

/**
 * Description: Test cases for AST testing about If / Loop - nested blocks
 * Author: Austin Zhang
 * Date: 2021/10/15 3:20 p.m.
 */
public class case3 {
  
  String str = "123";
  final String str1 = "3456";
  int a = 10;
  int b = 12;
  static final int c = 10;
  HashMap<String, String> id2name = new HashMap<>();

  public void foo() {
    int a = 20;
    int b = 30;
    int d = 10;
    System.out.println("123");
  }

}
