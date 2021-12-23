
public class Something {
    private Purse annPurse5;
    private Purse bobPurse7;
    @Before public void setUp() {
        modifiableEmptyMap = new HashMap<>();
        annPurse5 = new Purse(ANN, 5);
        bobPurse7 = new Purse(BOB, 7);
    }
    @Test public void mapMergeShouldReturnTheUnionWhenGivenDifferentSetsWithSomeCommonValues() {
        final Map<String, Purse> actual
            = Combiners.mapMerge(mapOf(annPurse5, bobPurse7), mapOf(bobPurse9, calPurse10), ADDITION);
        assertEquals(mapOf(annPurse5, bobPurse16, calPurse10), actual);
    }
    private static Map<String, Purse> mapOf(final Purse... values) {
        return mapOf(Purse::getOwner, values);
    }
    private static <K, V> Map<K, V> mapOf(final Function<V, K> keyMapper, final V... values) {
        return Stream.of(values).collect(Collectors.toMap(keyMapper, Function.identity()));
    }
}
        