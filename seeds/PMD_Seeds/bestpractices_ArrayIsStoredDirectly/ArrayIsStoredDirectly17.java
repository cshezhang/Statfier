
public class Foo {
    public class Inner {
        String [] arr;
        void foo (String[] x) {arr = x;}
    }
    public interface InnerInterface {
        class Nested {
            String [] arr;
            void foo (String[] x) {arr = x;}
        }
    }
    public enum InnerEnum {
        A;

        String [] arr;
        void foo (String[] x) {arr = x;}

        class Nested {
            String [] arr;
            void foo (String[] x) {arr = x;}
        }
    }
}
        