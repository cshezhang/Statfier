
public class B {
    public void bla() {
        try {
            ;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }
}
        