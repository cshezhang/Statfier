
public final class BacklogElementParameters {
    private final Long backlogId;
    private final String name;

    private BacklogElementParameters(final BacklogElementParameters.Builder builder) {
        this.backlogId = builder.backlogId;
        this.name = builder.name;
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
            return new BacklogElementParameters(this);
        }
    }
}
        