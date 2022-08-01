
public class Singleton {
    private static Singleton instance = null;
    public static Singleton getInstance() {
        synchronized(Singleton.class) {
            if (instance == null) {
                Singleton instance = new Instance();
                return instance;
            }
        }
        return instance;
    }
}
        