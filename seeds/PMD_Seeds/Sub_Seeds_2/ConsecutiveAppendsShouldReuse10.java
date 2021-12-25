
public class Foo {
    public void foo() {
        final StringBuffer stringBuffer; // declaration of the var is a different statement
        stringBuffer = new StringBuffer().append("agrego ").append("un ");
        stringBuffer.append("string ");
    }
}
        