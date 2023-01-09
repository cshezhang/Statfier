
/** The class comment */
public class CommentRequired implements Serializable {
    /** My list */
    List next;
    private static ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("next", List.class)};
}
        