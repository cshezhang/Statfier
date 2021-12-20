package iter0;

public class NullAssignmentFP {
    public void foo() {
        Type result;
        result = someHash.computeIfAbsent(a, _unused -> test ? truthy : null);
    }
}
        