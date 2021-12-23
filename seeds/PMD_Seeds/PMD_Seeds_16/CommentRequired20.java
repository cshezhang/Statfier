
/**
 * Comment required test class.
 */
public class CommentRequired {
    /**
     * Comment provided
     */
    @Override
    public void noComment() {
        Object o = new Object() {

            /**
             * @inheritDoc
             */
            @Override
            public String toString() {
                return "Inner Class";
            }
        };
    }
}
        