
public class Foo {
    public void m() {
        switch (s) {
            case 0 :
                s2 = s ;
                // missing break here!
            case 1 :
                s2 = (short)(s + 1);
                break;
            default :
                s2 = (short)(s + 2);
                // and missing break here!
        }
    }
}
        