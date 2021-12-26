
import java.math.RoundingMode;
public class Test {
    void doEnums() {
        RoundingMode mode1 = determineFirstMode();
        RoundingMode mode2 = determineSecondMode();
        if (mode1 == mode2) {}
    }
}
        