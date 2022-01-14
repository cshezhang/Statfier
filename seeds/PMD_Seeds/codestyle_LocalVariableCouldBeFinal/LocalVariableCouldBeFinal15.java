
public interface InterfaceWithDefaultMethod {
    default void bar() {
        String a = "a";
        System.out.println(a);
    }
}
        