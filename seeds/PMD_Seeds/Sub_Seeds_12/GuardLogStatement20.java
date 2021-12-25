
public class GuardLogStatement {
    public void foo() {
        double confidence = 0.5;
        int depth = (int) Math.ceil(-Math.log(1 - confidence) / Math.log(2));
    }
}
        