package net.consolejs.k8s.dashboard.entityview.repository;

import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

import java.util.Objects;

public abstract class AbstractDocument {
    @SerializedName("_id")
    private final ObjectId myObjectId;

    @SuppressWarnings("rawtypes")
    protected AbstractDocument(Builder builder) {
        myObjectId = builder.myObjectId;
    }

    public ObjectId getObjectId() {
        return myObjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractDocument that = (AbstractDocument) o;
        return Objects.equals(myObjectId, that.myObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myObjectId);
    }

    @SuppressWarnings("rawtypes")
    public static abstract class Builder<B extends AbstractDocument.Builder, T extends AbstractDocument> {
        private ObjectId myObjectId;

        protected Builder() {
            // Empty
        }

        protected Builder(T original) {
            myObjectId = original.getObjectId();
        }

        @SuppressWarnings("unchecked")
        public B withObjectId(ObjectId objectId) {
            myObjectId = objectId;
            return (B) this;
        }

        abstract public T build();
    }
}