
public class PmdBug {
    class Test {
        public String field;
        public Test t;
    }
    public void foo(String field) {
        Test t = new Test();
        t.field = field;
        t.t.field = field;
        t.field.toCharArray();
    }
    public static void main(String[] args) {
        new PmdBug().foo("Hello world");
    }
}
        