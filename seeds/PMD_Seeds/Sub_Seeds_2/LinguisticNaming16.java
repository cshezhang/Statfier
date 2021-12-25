
public class AttributeTypeAndNameIsInconsistentWithPrefixHave {
    int havexxx;
    int haveLegs; // violation
    boolean haveHorns;

    void myMethod() {
        int havexxxLocal;
        int haveLegsLocal; // violation
        boolean haveHornsLocal;
    }
}
        