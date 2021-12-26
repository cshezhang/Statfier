
public class OuterClass {

    public enum InnerEnum {
        INNER_ENUM("first", "second");
        private String[] titles;

        InnerEnum(String... titles) {
            this.titles = titles;
        }

        public String[] getTitles() {
            return titles;
        }
    }
}
        