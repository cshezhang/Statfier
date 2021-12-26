
import junit.framework.TestCase;
import org.junit.Test;

public class TournamentTest extends TestCase {
    Tournament tournament;

    @Test // note that this is also a junit 3 test, but the junit4 rule applies
    public void testGetBestTeam() {
    }

    @Test // this is just a junit 4 test
    public void getBestTeam() {
    }

    // this is ok
    @Test
    public void getBestTeamTest() {
    }
}
        