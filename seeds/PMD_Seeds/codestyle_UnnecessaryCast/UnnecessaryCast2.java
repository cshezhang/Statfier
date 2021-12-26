
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnnecessaryCastSample {
    public void localVars() {
        List<String> stringList = Arrays.asList("a");
        Iterator<String> stringIt = stringList.iterator();
        while (stringIt.hasNext()) {
            String element = (String) stringIt.next();
            String element2 = stringIt.next();
        }

        List<Double> doubleList = new ArrayList<>();
        Iterator<Double> doubleIt = doubleList.iterator();
        while (doubleIt.hasNext()) {
            Double number = (Double) doubleIt.next();
            Double number2 = doubleIt.next();
        }
    }
}
        