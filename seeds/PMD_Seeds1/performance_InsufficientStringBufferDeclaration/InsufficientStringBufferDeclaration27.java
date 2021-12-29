
public class Foo {
    public void bar(String str) {
        StringBuffer sb = new StringBuffer();
        switch (str.charAt(0)) {
            case 'a':
                sb.append("Switch block");
                break;
            case 'b':
                sb.append("Doesn't exceed");
                break;
            default:
                sb.append("16 chars");
        }
    }
    public void bar2(String str) {
        StringBuilder sb = new StringBuilder();
        switch (str.charAt(0)) {
            case 'a':
                sb.append("Switch block");
                break;
            case 'b':
                sb.append("Doesn't exceed");
                break;
            default:
                sb.append("16 chars");
        }
    }
}
        