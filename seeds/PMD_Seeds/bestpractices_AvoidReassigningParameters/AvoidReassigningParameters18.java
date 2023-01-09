
public class AvoidReassigningParameters {
    public void a(String b) {
        b = "1";
        b = "2"; // this is not reported, only the first assignment is
    }
}
        