
public class Foo implements Serializable
{
  private Foo(){}     //Compliant, private empty constructor intentionally used to prevent any direct instantiation of a class.
  public static void doSomething(){
    Foo foo = new Foo();
    ...
  }
  private void unusedPrivateMethod(){...}
  private void writeObject(ObjectOutputStream s){...}  //Compliant, relates to the java serialization mechanism
  private void readObject(ObjectInputStream in){...}  //Compliant, relates to the java serialization mechanism
}
