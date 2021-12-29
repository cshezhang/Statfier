
public class OuterClass {
    public int[] arrayReturningMethod() {
        class LocalClass {
            private String s;
            public String getString() {
                return this.s;
            }
        }
        LocalClass c = new LocalClass();
        return new int[0];
    }
}
        