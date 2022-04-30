public class Case5 {
    int getLiteral() {
        return 4;
    }

    public int getValue() {
        int array[] = new int[3];
        return array[getLiteral()]; // should report a warning here
    }
    
}