
public class Example extends View {
    private int foo;

    private Example(final Context context) {
        super(context);
    }

    public static void create() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Example example = new Example(null); // Should be marked since the constructor is private.
            }
        });
    }
}
        