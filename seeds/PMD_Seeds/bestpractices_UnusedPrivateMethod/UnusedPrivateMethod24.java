
public class PMDFalsePositiveTest {
    private <T> T doSomething(Object param){
        return (T) param;
    }

    public static void main(String[] args) {
        PMDFalsePositiveTest test = new PMDFalsePositiveTest();
        Object o = "Awesome!";
        String result = test.<String>doSomething(o);
        System.out.println(result);
    }
}
        