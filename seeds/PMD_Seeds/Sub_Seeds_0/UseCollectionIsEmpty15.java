
public enum ComponentSize {

    S("s");

    private String size;

    ComponentSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size;
    }

}
        