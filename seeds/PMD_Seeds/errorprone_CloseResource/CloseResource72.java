
public record MyRecord(boolean a) {
    public void foo () {
        record TestInnerRecord() {
            private static Object test;
        }
    }
}
        