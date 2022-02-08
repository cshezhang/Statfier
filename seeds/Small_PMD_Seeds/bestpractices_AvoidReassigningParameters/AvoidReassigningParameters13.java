
public class RealClass extends AbstractClass {
    public void setString(String string) {
        super.string = string; //violation
        this.string = string; // or violation
    }
}
        