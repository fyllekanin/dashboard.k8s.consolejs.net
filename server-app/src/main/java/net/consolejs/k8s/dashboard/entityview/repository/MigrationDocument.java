package net.consolejs.k8s.dashboard.entityview.repository;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MigrationDocument extends AbstractDocument {
    @SerializedName("name")
    private final String myName;

    private MigrationDocument(Builder builder) {
        super(builder);
        myName = builder.myName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return myName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MigrationDocument that = (MigrationDocument) o;
        return super.equals(that) &&
                Objects.equals(myName, that.myName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myName);
    }

    public Builder newBuilderFromCurrent() {
        return new Builder(this);
    }

    public static class Builder extends AbstractDocument.Builder<Builder, MigrationDocument> {
        private String myName;

        private Builder() {
        }

        private Builder(MigrationDocument original) {
            super(original);
            myName = original.myName;
        }

        public Builder withName(String name) {
            myName = name;
            return this;
        }

        public MigrationDocument build() {
            return new MigrationDocument(this);
        }
    }
}
