
public class MyAbstractClass implements Cloneable{
}
public class MyClonableClass extends MyAbstractClass{
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
        