
package net.sourceforge.pmd.lang.java.rule.performance.stringtostring;
public class Foo {
    public void bar() {
        User user = new User();
        String s = getId(user).toString();
    }
    public int getId(Car car) {
        return 0;
    }
    public String getId(User user) {
        return "";
    }
}
        