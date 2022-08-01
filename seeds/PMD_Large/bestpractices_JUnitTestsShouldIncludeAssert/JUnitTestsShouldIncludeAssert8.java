
import junit.framework.TestCase;
public abstract class AbstractAggregateCreatorTest extends TestCase{
    public abstract int getType();
    public abstract ProfileAggregate create(DatabaseTransaction db,
        DailyProfileList profiles, ProfileType type, ProfileStatus status)
        throws VixenException;
}
        