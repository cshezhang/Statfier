
public class VaragsFalsePositive {

    enum Sizes
    {
        TINY,
        MEDIUM
    }

    public boolean containsTiny(){
        return hasTiny(Sizes.MEDIUM, Sizes.TINY);
    }

    private boolean hasTiny(Sizes... sizes) {
        for (Sizes size : sizes) {
            if (size==Sizes.TINY) {
                return true;
            }
        }
        return false;
    }
}
        