
public class Useless {
    public boolean test(Useless team) {
        return (mNumber != null ? mNumber : team.mNumber)
            == (mKey != null ? mKey : team.mKey);
    }
}
        