
package foo;
public interface Foo1 {
    public class InnerClass {
        private InnerClass(){
        }
    }
    void method() {
        new InnerClass(); //Causes generation of accessor
    }
}
        