
interface Operation {
    Object apply();

    final Operation OPERATION = () -> { return null; };

    class Inner {
        int inner;
    }
}
        