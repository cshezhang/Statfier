
public abstract class MyADT {
    private MyADT() {
    }

    public abstract <R> R map(
            Function<String, ? extends R> onString,
            Function<Integer, ? extends R> onInt
    );

    public static final class StringHolder extends MyADT {
        private final String string;

        public StringHolder(String string) {
            this.string = string;
        }

        @Override
        public <R> R map(Function<String, ? extends R> onString, Function<Integer, ? extends R> onInt) {
            return onString.apply(string);
        }
    }

    public static final class IntHolder extends MyADT {
        private final Integer integer;

        public IntHolder(Integer integer) {
            this.integer = integer;
        }

        @Override
        public <R> R map(Function<String, ? extends R> onString, Function<Integer, ? extends R> onInt) {
            return onInt.apply(integer);
        }
    }
}
        