
public class Foo {
    public String foo(int in) {
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < in; i++) {
            switch (in) {
                case 0 :
                    continue;
                case 1:
                    retval.append("0");
                    continue;
                case 2:
                    retval.append("1");
                    retval.append("1");
                    continue;
                case 3:
                    retval.append("2");
                    continue;
                default:
                    retval.append("3");
                    retval.append("3");
                    continue;
            }
        }
        return retval.toString();
    }

    public String foo2(int in) {
        StringBuilder retval = new StringBuilder();
        for (int i = 0; i < in; i++) {
            switch (in){
                case 0 :
                    continue;
                case 1:
                    retval.append("0");
                    continue;
                case 2:
                    retval.append("1");
                    retval.append("1");
                    continue;
                case 3:
                    retval.append("2");
                    continue;
                default:
                    retval.append("3");
                    retval.append("3");
                    continue;
            }
        }
        return retval.toString();
    }
}
        