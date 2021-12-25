
public class DemoMissingBreakContinue {
    public DemoMissingBreakContinue() {
        method();
    }

    private void method() {
        for (int i = 0; i < 10; i = i + 1) {
            switch (i) {
            case 1:
                break;
            case 2:
                continue; //PMD complains about missing break which would be unreachable code
            default:
                break;
            }
        }
    }
}
        