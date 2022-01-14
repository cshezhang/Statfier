
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AppTest
{

   @ParameterizedTest
   @ValueSource(ints = {1, 2, 3})
   void test_not_null(int arg) {
     setupTax();
     assertNotNull(getTax(arg));
   }

    @ParameterizedTest
    @CsvSource({
        "1, 100",
        "2, 200",
        "3, 300",
    })
    void testLevels(int level, int health) {
        setLevel(level);
        runGame();
        assertEquals(playerHealth(), health);
    }
}
