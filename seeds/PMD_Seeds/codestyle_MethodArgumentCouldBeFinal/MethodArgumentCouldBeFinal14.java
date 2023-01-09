
public interface InterfaceWithClass {
    class Inner {
        public Inner(Object o) {
            Object a = o;
        }
        public Object justReturn(Object o) {
            return o;
        }
    }
}
        