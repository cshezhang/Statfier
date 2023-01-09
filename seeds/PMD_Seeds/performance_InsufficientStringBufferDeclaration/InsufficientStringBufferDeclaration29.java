
public class Foo {
    public void bar(char longnamedchar) {
        StringBuffer sb = new StringBuffer(1);
        sb.append(longnamedchar);
    }
    public void bar2(char longnamedchar) {
        StringBuilder sb = new StringBuilder(1);
        sb.append(longnamedchar);
    }
}
        