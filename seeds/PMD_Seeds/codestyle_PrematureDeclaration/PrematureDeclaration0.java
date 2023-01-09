
public class Bar {
    public int lengthSumOf(String[] strings) {

        int sum = 0;    // wasted cycles if strings have problems

        if (strings == null || strings.length == 0) return 0;

        for (int i=0; i<strings.length; i++) {
            sum += strings[i].length();
        }

        return sum;
    }
}
        