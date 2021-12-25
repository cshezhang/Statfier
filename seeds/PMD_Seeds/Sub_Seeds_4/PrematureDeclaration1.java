
public class Bar {
    public int lengthSumOf(String[] strings) {

        if (strings == null || strings.length == 0) return 0;

        int sum = 0;    // optimal placement

        for (int i=0; i<strings.length; i++) {
            sum += strings[i].length();
        }

        return sum;
    }
}
        