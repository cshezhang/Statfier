public class IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION {
    static class InnerClassSingleton extends IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION {
        static InnerClassSingleton singleton = new InnerClassSingleton();
    }

    static IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION foo = InnerClassSingleton.singleton;
}