
public class Violation {
    public void invalidSetAccessibleCalls() {
        this.getClass().getDeclaredConstructor(String.class).setAccessible(true);
        this.getClass().getDeclaredMethod("aPrivateMethod").setAccessible(true);
        this.getClass().getDeclaredField("aPrivateField").setAccessible(true);

        Violation.class.getDeclaredConstructor(String.class).setAccessible(true);
        Violation.class.getDeclaredMethod("aPrivateMethod").setAccessible(true);
        Violation.class.getDeclaredField("aPrivateField").setAccessible(true);
    }
}
        