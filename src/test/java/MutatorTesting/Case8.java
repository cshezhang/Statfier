package MutatorTestingCases;

public class Case8 {

    public boolean getCondition() {
        return true;
    }

    public void methodA() {
        boolean condition = getCondition();
        methodA();
//        if(condition) {
//            methodA();  // should report a warning here
//        } else {
//            methodA();  // And report a warning here
//        }
    }

}
