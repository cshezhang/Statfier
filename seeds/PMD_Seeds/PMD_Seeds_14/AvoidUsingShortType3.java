
public class TypeNameWithshort
{
    // … is not an infraction !
    int shortName = 0;

    public void avoidCommonMisinterpretation() {
        // … is not an infraction !
        int shortName = 0;
        TypeNameWithshort legal = new TypeNameWithshort(shortName);
    }
}
        