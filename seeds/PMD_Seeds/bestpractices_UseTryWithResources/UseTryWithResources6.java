
public class TryWithResources {
    public void run() {
        var noAutoclosable = new Object() {
            public void close() {}
        };
        try {
            System.out.println(noAutocloseable);
        } finally {
            noAutoclosable.close();
        }
    }
}
        