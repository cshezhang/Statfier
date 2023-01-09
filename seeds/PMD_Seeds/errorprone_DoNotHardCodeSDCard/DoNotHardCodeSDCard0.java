
public class MyActivity extends Activity {
    protected void bad() {
        String storageLocation = "/sdcard/mypackage";
    }

    protected void good() {
        String storageLocation = Environment.getExternalStorageDirectory() + "/mypackage";
    }
}
        