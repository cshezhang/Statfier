/*
JavadocStyle
scope = (default)private
excludeScope = (default)null
checkFirstSentence = (default)true
endOfSentenceFormat = ([.][ \t\n\r\f<])|([.]$)
checkEmptyJavadoc = (default)false
checkHtml = false
tokens = (default)ANNOTATION_DEF, ANNOTATION_FIELD_DEF, CLASS_DEF, CTOR_DEF, \
         ENUM_CONSTANT_DEF, ENUM_DEF, INTERFACE_DEF, METHOD_DEF, PACKAGE_DEF, \
         VARIABLE_DEF, RECORD_DEF, COMPACT_CTOR_DEF


*/

package com.puppycrawl.tools.checkstyle.checks.javadoc.javadocstyle;

public class InputJavadocStyleFirstSentenceFormatFour {
  /** Link to some page in two lines. <a href="someLink"/> */
  void tagInTwoLines() {} // ok

  /**
   * This Javadoc contains unclosed tag.
   * <code>unclosed 'code' tag<code>
   */
  private void unclosedTag() {} // ok

  void javadocLikeCommentInMethod() { // ok
    /** It pretends to be Javadoc without dot, but it's just comment in method */
    final int i = 0; // ok
  }
  // violation below
  /** {@inheritDoc} */
  private void inheritDoc() {}

  /** <b>Note:<b> it's unterminated tag. */
  private void unterminatedTag() {} // ok

  /** // violation Javadoc without dot */
  public interface TestInterface {
    /** // violation Javadoc without dot */
    void method();
  }

  static class TestStaticClass { // ok
    /** // violation Javadoc without dot */
    public int field;
  }

  /**
   * .
   *
   * @throws Exception if an error occurs
   */
  void foo() throws Exception {} // ok

  /**
   * // violation text /
   *
   * @throws Exception if an error occurs
   */
  void bar() throws Exception {}

  /**
   * {@inheritDoc}
   *
   * <p>
   *
   * @throws IllegalArgumentException with errMsg as its message
   */
  void inheritDocWithThrows() {} // ok

  /**
   * // violation /
   *
   * <p>*
   *
   * @param s
   * @return Return
   */
  public int test(String s) {
    return 0;
  }

  /** Set of all class field names. */
  public String field; // ok

  /**
   * Test.
   *
   * <pre class="body">
   * for (
   * ; i &lt; j; i++, j--) {}
   * </pre>
   */
  public void test2() {} // ok

  /**
   * Test.
   *
   * <pre><code>&#064;ExtendWith(SpotBugsExtension.class)
   * public class SampleTest {
   * }</code></pre>
   */
  public void test3() {} // ok
}

