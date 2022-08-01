
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        String foo = "blah";
        int count = 0;
        if (true) {
            sb.append("CCC" + (++count) + "Ffalsd");
        } else if (foo.length() == 2) {
            sb.append("CCC" + (++count) + "Ffalsd");
        }
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        String foo = "blah";
        int count = 0;
        if (true) {
            sb.append("CCC" + (++count) + "Ffalsd");
        } else if(foo.length() == 2) {
            sb.append("CCC" + (++count) + "Ffalsd");
        }
    }
}
        