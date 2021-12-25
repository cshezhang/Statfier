
import android.os.Build;

public class Test {

    public String test() {
        print(String.valueOf(Build.TIME));
        print(String.valueOf(Build.VERSION.SDK_INT));
    }

    private void print(String s) {
        System.out.println(s);
    }
}
        