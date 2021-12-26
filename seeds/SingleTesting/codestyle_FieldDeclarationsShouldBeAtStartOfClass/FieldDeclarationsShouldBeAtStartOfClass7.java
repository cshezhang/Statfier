
public class MyClass {
    private final String name;
    public MyClass() {
        this.name = "whatever";
    }
    public void onCreate() {
        someView.setListener(onSomeViewClick);
    }
    private final OnClickListener onSomeViewClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // something
        }
    };
}
        