
public class Foo1 {
    public class InnerClass {
        private InnerClass(int[] a){
        }
    }
    void method() {
        new InnerClass(new int[]{1}); //Causes generation of accessor
    }
}
        