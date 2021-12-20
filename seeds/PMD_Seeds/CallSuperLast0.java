package iter0;

public class MyActivity extends Activity {
    @Override
    protected void onPause() {
        foo();
        super.onPause();
    }
}
        