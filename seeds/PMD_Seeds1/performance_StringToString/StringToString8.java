
public class Foo {
    public static String toString(MyObject o) {
        return "MyObject: " + o;
    }

    public static vod main(String[] args) {
        //in your code
        MyObject o = new MyObject(); //MyObject has no relation with String
        System.out.println(Foo.toString(o)); //PMD violation false positive
    }
}
        