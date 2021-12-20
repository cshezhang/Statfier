package iter0;

public class Foo implements Serializable {

    List next;

    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("next", List.class)};

}
        