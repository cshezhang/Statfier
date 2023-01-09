

@Deprecated // OK
@SomeArrays(pooches={DOGS.LEO}) // Violation - COMPACT_NO_ARRAY
@SuppressWarnings({""}) // Violation - COMPACT_NO_ARRAY
public class TestOne
{

}

@SomeArrays(pooches={DOGS.LEO}, um={}, test={"bleh"}) // Violation - COMPACT_NO_ARRAY
@SuppressWarnings("") // OK
@Deprecated() // Violation - cannot have closing parenthesis
class TestTwo {

}
        