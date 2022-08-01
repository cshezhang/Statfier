
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

public class TournamentTest {
    @Test // note that this is also a junit 3 test, but the junit 5 rule applies
    public void testGetBestTeam() {
    }

    @Test // this is just a junit 5 test
    public void getBestTeam() {
    }

    @ParameterizedTest // this is a paramterized junit 5 test
    public void getWorstTeam(String param) {
    }

    // this is ok
    @Test
    public void getBestTeamTest() {
    }
}
        