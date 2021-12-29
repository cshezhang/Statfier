
public class Useless {
    public boolean test(Useless team) {
        return (mNumber != null ? mNumber.equals(team.mNumber) : team.mNumber == null)
            && (mKey != null ? mKey.equals(team.mKey) : team.mKey == null)
            && (mTemplateKey != null ? mTemplateKey.equals(team.mTemplateKey) : team.mTemplateKey == null)
            && (mName != null ? mName.equals(team.mName) : team.mName == null);
    }
}
        