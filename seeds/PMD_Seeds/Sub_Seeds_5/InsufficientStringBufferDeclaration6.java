
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer();
        if(true){
            sb.append("1234567890");
        } else {
            sb.append("123456789");
        }
    }

    public void bar2(List l) {
        StringBuilder sb = new StringBuilder();
        if(true){
            sb.append("1234567890");
        } else {
            sb.append("123456789");
        }
    }
}
        