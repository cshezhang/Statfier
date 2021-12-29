
public class B {
    public void bla() {
        try {
            try {
                ;
            } catch (IllegalStateException ex) {
                throw new RuntimeException("Oh no!");
            }
        } catch (IllegalStateException ex) {
            throw new RuntimeException(ex);
        }
    }
}
        