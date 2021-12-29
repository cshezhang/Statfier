
public class Singleton {
    private static Singleton instance = null;
    public static Singleton getInstance() {
        synchronized(Singleton.class){
            if (instance == null) {
                instance = new Instance();
            }
        }
        return instance;
    }
}
        