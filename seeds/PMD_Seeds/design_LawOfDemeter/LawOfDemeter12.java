
public class Test {
    // violation, because object is used
    public Object create2() {
        Object o = myObjectCreator.create();
        o.setName("my name");                    // < - - !!!
        return o;
    }
}
        