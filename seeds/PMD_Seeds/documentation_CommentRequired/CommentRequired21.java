
/**
 * Comment required test class.
 */
public class CommentRequired {

    private int foo;

    public int getFoo() {
        Object o = new Object() {

            String foox;

            public String getFoox() {
                return foox;
            }
        };
        return foo;
    }

    public void setFoo(int x) {
        foo = x;
    }
}
        