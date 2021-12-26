
public class Foo {
    void bar() {
        if (super.guardStmtByLogLevel.isEmpty() && logLevels.length > 0 && guardMethods.length > 0) {
            configureGuards(logLevels, guardMethods);
        }
    }
}
        