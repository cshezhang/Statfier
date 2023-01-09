
import java.util.*;

public class ConcatInLoop {
    public void good1() {
        int log = 0;
        List<Integer> values = Arrays.asList(new Integer[]{1, 2, 3});
        for (int val : values) {
            log = log + val;
        }
        Iterator<Integer> iter = values.iterator();
        while (iter.hasNext()) {
            log = log + iter.next();
        }
    }

    public void good2() {
        int log = 0;
        List<Integer> values = Arrays.asList(new Integer[]{1, 2, 3});
        for (int val : values) {
            log += val;
        }
    }

    public void good3() {
        double totalParticipationPercentage = 0;
        for (Object portfolioByCategory : new ArrayList()) {
            for (Object portfolioInstrumentDetails : new ArrayList()) {
                totalParticipationPercentage = totalParticipationPercentage
                        + (double) portfolioInstrumentDetails.hashCode();
            }
        }
    }

    public int good4(String keyName) {
        int index = 0;
        HashMap<String, String> columnsTypes = new HashMap<String, String>();
        for (String variableName : columnsTypes.keySet()) {
            if (keyName.equals(variableName)) {
                return index;
            }
            index += 1;
        }
    }

    public long good5(String keyName) {
        long index = 0;
        HashMap<String, String> columnsTypes = new HashMap<String, String>();
        for (String variableName : columnsTypes.keySet()) {
            if (keyName.equals(variableName)) {
                return index;
            }
            index += 1;
        }
    }
}
        