package iter0;

public class MyAbstractClass implements Cloneable{
}
public class MyClonableClass extends MyAbstractClass{
    protected java.lang.Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
        