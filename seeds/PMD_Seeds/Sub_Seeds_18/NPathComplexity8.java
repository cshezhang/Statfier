
public class NPathTest {
    Product buildProduct(String title, Language language) {
        if (title.length() > 100 || title.length() == 0) {
            throw new IllegalArgumentException("Invalid title length (1-100 chars): " + title);
        }
        var product = new Product();
        var description = switch (language) {
            case FR -> description.setFr(title);
            case IT -> description.setIt(title);
            case EN -> description.setEn(title);
            default -> description.setDe(title);
        };
        product.setDescription(description);
        product.setMagicNumber(3);
        return product;
    }
}
        