
/**
 * Comment required test class.
 */
public class CommentRequired {
    @Override
    public void noComment() {
        Object o = new Object() {

            @Override
            public String toString() {
                return "Inner Class";
            }
        };
    }
}
        