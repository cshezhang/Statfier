
public class GuardLogStatement {
    public String foo() {
        return ThisIsNotALogger.error("message " + this);
    }
}
        