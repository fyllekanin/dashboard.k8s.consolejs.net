package net.consolejs.k8s.dashboard.entityview.repository.user;

import com.google.gson.annotations.SerializedName;
import net.consolejs.k8s.dashboard.entityview.repository.AbstractDocument;

import java.util.Objects;

public class UserDocument extends AbstractDocument {
    @SerializedName("username")
    private final String myUsername;
    @SerializedName("password")
    private final String myPassword;

    private UserDocument(Builder builder) {
        super(builder);
        myUsername = builder.myUsername;
        myPassword = builder.myPassword;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Builder newBuilderFromCurrent() {
        return new Builder(this);
    }

    public String getUsername() {
        return myUsername;
    }

    public String getPassword() {
        return myPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDocument that = (UserDocument) o;
        return super.equals(that) &&
                Objects.equals(myUsername, that.myUsername) &&
                Objects.equals(myPassword, that.myPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myUsername, myPassword);
    }

    public static class Builder extends AbstractDocument.Builder<Builder, UserDocument> {
        private String myUsername;
        private String myPassword;

        private Builder() {
            super();
        }

        private Builder(UserDocument document) {
            super(document);
            myUsername = document.myUsername;
            myPassword = document.myPassword;
        }

        public Builder withUsername(String username) {
            myUsername = username;
            return this;
        }

        public Builder withPassword(String password) {
            myPassword = password;
            return this;
        }

        public UserDocument build() {
            return new UserDocument(this);
        }
    }
}
