
package pmdtests;

public class Foo {
    private final int i;

    public Foo(int i) {
        this.i = i;
    }

    public void meth() {
        System.out.println(this.i);     // wrong violation: method chain calls
        System.out.println(super.i);    // wrong violation: method chain calls
        System.out.println(i);          // no violation
    }

    public void method(Foo this) {      // receiver param!!
        System.out.println(this.i);     // wrong violation: method chain calls
    }

    public void method(Foo this, String... args) {
        System.out.println(this.i);     // wrong violation: method chain calls
        for (String arg: args) {
            System.out.println(arg);
        }
    }
}
        