
public class Foo {
    public void bar(String str) {
        StringBuffer sb = new StringBuffer();
        switch (str.charAt(0)) {
            case 'a':
                sb.append("Switch block");
                break;
            default:
                sb.append("The default block exceeds 16 characters and will fail");
        }
    }
    public void bar2(String str) {
        StringBuilder sb = new StringBuilder();
        switch (str.charAt(0)) {
            case 'a':
                sb.append("Switch block");
                break;
            default:
                sb.append("The default block exceeds 16 characters and will fail");
        }
    }
}
        