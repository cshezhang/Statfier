
interface TestInterface extends Cloneable {
    TestInterface clone();
}

class CloneableClass implements TestInterface {
    @Override // creates a warning though CloneableClass is actually implementing Cloneable
    public CloneableClass clone() {
        // clone implementation
    }
}
        