
class MultiCatch {

    public static void main(String[] args) {
        try {
            Class.forName("org.example.Foo").newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException eMultiCatch) {
            throw new UnsupportedOperationException(eMultiCatch);
        }
    }
}
        