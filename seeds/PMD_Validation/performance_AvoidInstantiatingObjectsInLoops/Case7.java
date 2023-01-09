package MutatorTestingCases;

import java.util.ArrayList;

public class Case7 {

    static class Foo {}

    public void test() {
        ArrayList<Object> strs = new ArrayList<>();
        strs.forEach(str->{
            Foo foo = new Foo();  // should report a warning here
        });
    }

}
