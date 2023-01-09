
public class UnusedAssignmentUnusedVariableFP {
    public void case1() {
        String notUnused;
        {
            notUnused = "a";
            System.out.println(notUnused);
        }
    }
    public int case2() {
        int start;
        if (true)
            start = 1;
        else
            start = 2;
        return start;
    }
    private boolean case3(int number) {
        String stringNumber = String.valueOf(number);
        int divRest;
        if (stringNumber.length() == 3)
            divRest = number % 10;
        else if (stringNumber.length() == 4)
            divRest = number % 100;
        else
            divRest = number % 10_000;
        if (divRest == 0)
            return true;
        return number % 2 == 0;
    }
}
        