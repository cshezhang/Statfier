
public class Foo {
    public void bar(char longnamedchar) {
        StringBuffer sb = new StringBuffer(132+42);
        sb.append();
        StringBuffer sb1 = new StringBuffer(132*42);
        sb1.append();
    }

    public void bar2(char longnamedchar) {
        StringBuilder sb = new StringBuilder(132+42);
        sb.append();
        StringBuilder sb1 = new StringBuilder(132*42);
        sb1.append();
    }
}
        