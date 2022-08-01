
public class AttributeTypeAndNameIsInconsistentWithPrefixHas {
    int haskell;
    int hasMoney; // violation
    boolean hasSalary;

    void myMethod() {
        int haskellLocal;
        int hasMoneyLocal; // violation
        boolean hasSalaryLocal;
    }
}
        