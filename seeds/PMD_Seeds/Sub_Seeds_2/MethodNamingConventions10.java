
import org.junit.Test; // note: test case uses "useAuxClasspath=false"!!

public class TournamentTest {
    Tournament tournament;

    // wrong test name pattern
    @Test
    public void get_best_team() {
    }

    // this is ok
    @Test
    public void getBestTeam() {
    }
}
        