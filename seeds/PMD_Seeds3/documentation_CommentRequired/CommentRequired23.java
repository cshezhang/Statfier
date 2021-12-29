
/**
 * Comment required test class.
 */
public class CommentRequired {

    private int foo;

    /**
     * @return foo
     */
    public int getFoo() {
        Object o = new Object() {

            String foox;
            /**
             * @return foox
             */
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
        