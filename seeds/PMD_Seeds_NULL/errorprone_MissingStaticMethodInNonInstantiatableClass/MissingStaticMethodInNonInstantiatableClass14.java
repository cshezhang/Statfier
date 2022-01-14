
public final class BacklogElementParameters {
    private final Long backlogId;
    private final String name;

    private BacklogElementParameters(Long backlogId, String name) {
        this.backlogId = backlogId;
        this.name = name;
    }

    public static class Builder {
        public Builder backlogId(final Long backlogId) {
            this.backlogId = backlogId;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public BacklogElementParameters build() {
            return new BacklogElementParameters(backlogId, name);
        }
    }
}
        