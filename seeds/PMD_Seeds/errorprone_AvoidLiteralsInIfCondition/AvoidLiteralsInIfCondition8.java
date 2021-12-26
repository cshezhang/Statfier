
public class AvoidLiteralsInIfConditionWithExpressions {
    public void test() {
        if (1) {}    // ok, "1" is in ignoreMagicNumbers
        if (1+1) {}  // not ok! multiple literals in expression
        if (a+1) {}  // ok, single literal, whitelisted
        if (bodyStart >= 0 && bodyStart != (currentToken.length() - 1)) {} // ok, single literal per expression, both whitelisted
        if (1 * 5) {} // not ok - literal 5 and also a expression with two literals
        if (a + 5) {} // not ok
        if (i == a + 5) {} // not ok - literal 5
        if (i == 1 + 5) {} // not ok - expression with two literals
        if (s.equals("Prefix" + "Suffix")) {}
    }
}
        