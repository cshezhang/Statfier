package MutatorTestingCases;

public class Case9 {

    public void useStringBufferLength() {
        StringBuffer sb = new StringBuffer("Hello PMD!");
        sb.toString().equals("");
//        final String str = "";
//        sb.toString().equals(str); // should report a warning here
    }

}
