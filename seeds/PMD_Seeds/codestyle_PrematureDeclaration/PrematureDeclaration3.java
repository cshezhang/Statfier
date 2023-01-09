
public class PrematureDeclarationClassCastException {
    public void bar() {
        int switchvar = 0;
        switch (switchvar) {
            case 1:
                boolean localvar;
                break;
            case 2:
                localvar = false;
                if (localvar) {
                    //
                }
        }
    }
}
        