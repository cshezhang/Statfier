
public class Foo {

    private CodeException[] getCodeExceptions() {
        int             size  = exception_vec.size();
        CodeException[] c_exc = new CodeException[size];

        try {
            for (int i=0; i < size; i++) {
                CodeExceptionGen c = (CodeExceptionGen)exception_vec.get(i);
                c_exc[i] = c.getCodeException(cp);
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
        return c_exc;
    }
}
        