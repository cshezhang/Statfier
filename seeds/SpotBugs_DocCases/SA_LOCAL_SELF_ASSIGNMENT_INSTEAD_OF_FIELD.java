public class SA_LOCAL_SELF_ASSIGNMENT_INSTEAD_OF_FIELD {

    int foo;
    public void setFoo(int foo) {
        foo = foo;
    }
    
}