
public class Foo {
    toString2(char[] a, int n){
        StringBuffer sb = new StringBuffer();
        sb.append("ab");
        sb.append(a, 0, n);
        sb.append('c');
        return sb.toString();
    }

    toString3(char[] a, int n){
        StringBuilder sb = new StringBuilder();
        sb.append("ab");
        sb.append(a, 0, n);
        sb.append('c');
        return sb.toString();
    }
}
        