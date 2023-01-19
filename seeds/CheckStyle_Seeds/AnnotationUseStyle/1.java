@Deprecated // Violation - must have closing parenthesis
@SomeArrays(pooches = {DOGS.LEO}) // OK
@SuppressWarnings({""}) // Violation - EXPANDED
public class TestOne {}

@SomeArrays(
    pooches = {DOGS.LEO},
    um = {},
    test = {"bleh"}) // OK
@SuppressWarnings("") // Violation - EXPANDED
@Deprecated() // OK
class TestTwo {}

