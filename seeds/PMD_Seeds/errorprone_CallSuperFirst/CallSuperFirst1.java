
public class MyActivity extends Activity {
    @Override
    protected void onCreate(final Bundle state) {
        super.onCreate(state);
        foo();
    }
    @Override
    protected void onResume() {
        super.onResume();
        foo();
    }
    @Override
    protected void onStart() {
        super.onStart();
        foo();
    }
}
        