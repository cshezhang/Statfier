
public class Stuff {
    @SuppressWarnings("unused")
    public void doStuff() throws SomeException {
        try {
            doMoreStuff();
        } catch (Exception e) {
            StringBuffer irrelevantSB = new StringBuffer("Irrelevant").append(" string").append(" buffer");
            SomeException someException = new SomeException(e);
            throw someException;
        }
    }

    private void doMoreStuff() {
        // Stuff happens
    }
}
        