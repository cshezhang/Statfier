
public class Suit {
    private final String name;
        private Suit(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    public static final Suit CLUBS = new Suit("Clubs");
    public static final Suit DIAMONDS = new Suit("Diamonds");
    public static final Suit HEARTS = new Suit("Hearts");
    public static final Suit SPADES = new Suit("Spades");
}
        