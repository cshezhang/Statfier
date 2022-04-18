
public class blabla implements blabla2 {
    @Override
    public List<String> getProductImageUrls(final ApparelStyleVariantProductModel product, final String format) {
        return getImageUrlsListForVariant(product, format);
    }
    private List<String> getImageUrlsListForVariant(final VariantProductModel variant, final String format) {
        final SortedMap<Integer, String> imageUrls = getImageUrlsMapForVariant(variant, format);
        return new ArrayList<String>(imageUrls.values());
    }
}
        