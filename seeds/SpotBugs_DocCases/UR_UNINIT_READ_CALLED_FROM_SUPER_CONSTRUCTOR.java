abstract class A {
    int hashCode;
    abstract Object getValue();

    A() {
        hashCode = getValue().hashCode();
    }
}

class B extends A {
    Object value;

    B(Object v) {
        this.value = v;
    }

    Object getValue() {
        return value;
    }
}