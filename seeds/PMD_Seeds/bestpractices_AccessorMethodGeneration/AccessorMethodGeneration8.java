
public class Foo implements Parcelable {
    public static final Creator<Foo> CREATOR = new Creator<Foo>() {
        @Override
        public Foo createFromParcel(Parcel source) {
            return new Foo(source.readString(),
                          getBooleanForInt(source.readInt()),
                          source.readLong());
        }

        @Override
        public Foo[] newArray(int size) {
            return new Foo[size];
        }

        private boolean getBooleanForInt(int value) {
            return value == 1;
        }
    };
}
        